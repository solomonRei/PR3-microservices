//package com.pr.parser.config;
//
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Flux;
//
//import java.util.List;
//
//@Configuration
//public class RabbitMQLoadBalancerConfig {
//
//    @Bean
//    public ServiceInstanceListSupplier serviceInstanceListSupplier() {
//        return new RabbitMQServiceInstanceListSupplier();
//    }
//
//    static class RabbitMQServiceInstanceListSupplier implements ServiceInstanceListSupplier {
//
//        @Override
//        public String getServiceId() {
//            return "rabbitmq";
//        }
//
//        @Override
//        public Flux<List<ServiceInstance>> get() {
//            return Flux.just(
//                    List.of(
//                            createInstance("rabbitmq1", 5672),
//                            createInstance("rabbitmq2", 5673),
//                            createInstance("rabbitmq3", 5674)
//                    )
//            );
//        }
//
//        private ServiceInstance createInstance(String host, int port) {
//            return new ServiceInstance() {
//                @Override
//                public String getServiceId() {
//                    return "rabbitmq";
//                }
//
//                @Override
//                public String getHost() {
//                    return host;
//                }
//
//                @Override
//                public int getPort() {
//                    return port;
//                }
//
//                @Override
//                public boolean isSecure() {
//                    return false;
//                }
//
//                @Override
//                public java.net.URI getUri() {
//                    return java.net.URI.create("http://" + host + ":" + port);
//                }
//
//                @Override
//                public java.util.Map<String, String> getMetadata() {
//                    return java.util.Collections.emptyMap();
//                }
//            };
//        }
//    }
//}
