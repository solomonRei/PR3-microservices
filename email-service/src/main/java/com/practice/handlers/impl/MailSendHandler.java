package com.practice.handlers.impl;

import com.practice.events.Event;
import com.practice.events.impl.MailSendEvent;
import com.practice.handlers.Handler;
import com.practice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Handler for sending mail. */
@Component
@RequiredArgsConstructor
public class MailSendHandler implements Handler {

  private final EmailService emailService;

  @Override
  public void handle(Event event) {
    emailService.sendMessage((MailSendEvent) event);
    System.out.println("Mail sent successfully");
  }
}
