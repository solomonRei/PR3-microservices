package com.practice.controllers;

import com.practice.events.impl.MailSendEvent;
import com.practice.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for email related operations. */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/email")
public class EmailController {

  private final EmailService emailService;
  private final Environment env;

  @PostMapping("/emailQueue")
  public ResponseEntity<String> emailQueue() {
    emailService.processMessagesToQueue();
    return ResponseEntity.ok("Events successfully processed from the queue.");
  }

  @PostMapping("/addQueue")
  public ResponseEntity<String> addQueue(@RequestBody MailSendEvent mailEvent) {
    log.info("Received email to send: " + mailEvent.getSubject());

    return ResponseEntity.ok("Email queued successfully");
  }

  @GetMapping
  public ResponseEntity<String> displayPort() {
    String serverPort = env.getProperty("server.port", "Default port (not set)");
    return ResponseEntity.ok("Application is running on port: " + serverPort);
  }
}
