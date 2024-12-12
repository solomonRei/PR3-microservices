package com.practice.config;

import java.util.Properties;

import com.practice.config.properties.EmailDefaultProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/** Configuration for mail sender. */
@Configuration
public class MailConfig {


  /**
   * Java mail sender.
   *
   * @return JavaMailSender
   */
  @Bean
  public JavaMailSender javaMailSender(EmailDefaultProperties emailDefaultProperties) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);

    mailSender.setUsername(emailDefaultProperties.getDefaultFrom());
    mailSender.setPassword(emailDefaultProperties.getDefaultFromPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
