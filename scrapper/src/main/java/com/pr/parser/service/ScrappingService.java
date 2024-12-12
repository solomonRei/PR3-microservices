package com.pr.parser.service;

import com.pr.parser.enums.Currency;
import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;
import com.pr.parser.specs.ProductSpecificationFactory;
import com.pr.parser.utils.PriceConverterUtils;
import com.pr.parser.validation.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrappingService {

    private final WebClientService webClientService;

    private final ProductValidator productValidator;

    private final ProductSpecificationFactory productSpecificationFactory;

    private final PhonePublisher phonePublisher;

    public Flux<Product> parseHtmlForProducts(String html) {
        Document document = Jsoup.parse(html);
        Elements productElements = document.select(".js-itemsList-item");

        return Flux.fromIterable(productElements)
                .flatMap(productElement -> {
                    var productName = productElement.select("meta[itemprop=name]").attr("content");
                    var productPrice = productElement.select(".card-price_curr").text();
                    var productLink = productElement.select("a[itemprop=url]").attr("href");

                    var product = Product.builder()
                            .name(productName)
                            .price(productPrice)
                            .link(productLink)
                            .currency(Currency.MDL)
                            .build();

                    productValidator.validate(product);

                    return scrapeAdditionalData(product.getLink())
                            .map(characteristics -> {
                                product.setCharacteristics(characteristics);
                                return product;
                            });
                });
    }

    public Mono<Map<String, String>> scrapeAdditionalData(String productLink) {
        System.out.println("Scraping additional data for product: " + productLink);
        return webClientService.fetchHtmlContent(productLink)
                .map(html -> parseCharacteristics(Jsoup.parse(html)));
    }

    private Map<String, String> parseCharacteristics(Document document) {
        return document.select("div.tab-pane-inner")
                .select("table")
                .select("tr")
                .stream()
                .map(row -> row.select("td"))
                .filter(columns -> columns.size() == 2)
                .collect(
                        HashMap::new,
                        (map, columns) -> map.put(columns.get(0).text(), columns.get(1).text()),
                        HashMap::putAll
                );
    }

    public Mono<FilteredProductsResult> processProducts(List<Product> products, String params) {
        Instant timestamp = Instant.now();

        var priceSpec = productSpecificationFactory.createCombinedSpecificationFromSearch(params);
        return Flux.fromIterable(products)
                .filter(priceSpec::isSatisfiedBy)
                .flatMap(product -> {
                    var convertedPrice = PriceConverterUtils.convertPrice(new BigDecimal(product.getPrice()), Currency.MDL, Currency.EUR);
                    product.setPrice(convertedPrice.toString());
                    product.setCurrency(Currency.EUR);
                    return Mono.just(product);
                })
                .collectList()
                .flatMap(filteredProducts -> {
                    var totalSum = filteredProducts.stream()
                            .map(product -> new BigDecimal(product.getPrice()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return Mono.just(new FilteredProductsResult(filteredProducts, totalSum, timestamp));
                });
    }


    public Mono<List<Product>> getAllProducts() {
        return webClientService.fetchHtmlContent("/ru/catalog/electronics/telephones/mobile/?page_=page_3")
                .flatMapMany(this::parseHtmlForProducts)
                .collectList();
    }

    public Mono<FilteredProductsResult> getFilteredProducts(String params) {
        return webClientService.fetchHtmlContent("/ru/catalog/electronics/telephones/mobile/?page_=page_3")
                .flatMapMany(this::parseHtmlForProducts)
                .collectList()
                .flatMap(products -> processProducts(products, params))
                .flatMap(result -> phonePublisher.publishFilteredProductsResult(result)
                        .then(saveToFile(result))
                        .flatMap(filePath -> uploadToFTPServer(filePath))
                        .thenReturn(result))
                .doOnError(error -> System.err.println("Error during processing: " + error.getMessage()));
    }

    private Mono<String> saveToFile(FilteredProductsResult result) {
        return Mono.fromCallable(() -> {
            String fileName = "filtered_products_" + System.currentTimeMillis() + ".txt";
            File file = new File(fileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(result.toString());
            } catch (IOException e) {
                throw new RuntimeException("Error writing to file", e);
            }

            return file.getAbsolutePath();
        });
    }

    private Mono<Void> uploadToFTPServer(String filePath) {
        return Mono.fromRunnable(() -> {
            FTPClient ftpClient = new FTPClient();

            try {
                ftpClient.connect("ftp_server", 21);
                ftpClient.login("testuser", "testpass");
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                if (!ftpClient.changeWorkingDirectory("/uploads")) {
                    boolean dirCreated = ftpClient.makeDirectory("/uploads");
                    if (!dirCreated) {
                        throw new RuntimeException("Failed to create directory on FTP server");
                    }
                    ftpClient.changeWorkingDirectory("/uploads");
                }

                String remoteFileName = "data.txt";

                File file = new File(filePath);
                try (var inputStream = new FileInputStream(file)) {
                    boolean success = ftpClient.storeFile(remoteFileName, inputStream);
                    if (!success) {
                        throw new RuntimeException("Failed to upload file to FTP server");
                    }
                    System.out.println("File successfully uploaded and overwritten as " + remoteFileName);
                }

                ftpClient.logout();
            } catch (IOException e) {
                throw new RuntimeException("Error during FTP upload", e);
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    System.err.println("Error disconnecting FTP client: " + e.getMessage());
                }
            }
        });
    }



}
