package com.pr.parser.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AsyncFtpFileService {

    private static final String FTP_HOST = "ftp_server";
    private static final int FTP_PORT = 21;
    private static final String FTP_USER = "testuser";
    private static final String FTP_PASS = "testpass";
    private static final String REMOTE_FILE_PATH = "/uploads/data.txt";
    private static final String LOCAL_FILE_PATH = "/app/resources/uploads.txt";
    private static final String LAB2_SERVER_URL = "http://http-server-1:8082/product/upload";

    private final WebClient webClient;

    @Async
    public void fetchFileFromFTP() {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(FTP_HOST, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASS);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            System.out.println("Connected to FTP server. Fetching file...");

            try (FileOutputStream fos = new FileOutputStream(LOCAL_FILE_PATH)) {
                boolean success = ftpClient.retrieveFile(REMOTE_FILE_PATH, fos);
                if (success) {
                    System.out.println("File fetched successfully: " + LOCAL_FILE_PATH);
                    sendFileToLab2Server(new File(LOCAL_FILE_PATH));
                } else {
                    System.err.println("Failed to fetch file from FTP server.");
                }
            }

            ftpClient.logout();
        } catch (IOException e) {
            System.err.println("Error occurred while fetching file: " + e.getMessage());
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException ex) {
                System.err.println("Error disconnecting FTP client: " + ex.getMessage());
            }
        }
    }

    private void sendFileToLab2Server(File file) {
        if (!file.exists()) {
            System.err.println("File does not exist for upload: " + file.getAbsolutePath());
            return;
        }

        try {
            System.out.println("Sending file to LAB2 server...");

            webClient.post()
                    .uri(LAB2_SERVER_URL)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", new FileSystemResource(file)))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnSuccess(response -> System.out.println("Successfully sent file to Lab2: " + file.getName()))
                    .doOnError(error -> System.err.println("Failed to send file to Lab2: " + error.getMessage()))
                    .block();

        } catch (Exception e) {
            System.err.println("Error sending file to LAB2 server: " + e.getMessage());
        }
    }
}

