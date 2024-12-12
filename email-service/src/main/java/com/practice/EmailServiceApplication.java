package com.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/** The main class for the email service application. */
@EnableScheduling
@SpringBootApplication
@EnableRetry
@EnableAspectJAutoProxy
public class EmailServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(EmailServiceApplication.class, args);
  }
}
