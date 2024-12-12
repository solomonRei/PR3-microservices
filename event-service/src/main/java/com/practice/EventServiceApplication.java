package com.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main class for the event service application.
 */
@EnableScheduling
@SpringBootApplication
public class EventServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(EventServiceApplication.class, args);
  }
}
