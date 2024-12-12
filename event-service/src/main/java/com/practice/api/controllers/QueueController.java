package com.practice.api.controllers;

import com.practice.api.dto.EventRequest;
import com.practice.domain.events.Event;
import com.practice.domain.services.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for the queue. */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = QueueController.BASE_PATH)
public class QueueController {

  private final QueueService queueService;
  private final Environment env;

  protected static final String BASE_PATH = "/api/event";

  @PostMapping("/addQueue")
  public ResponseEntity<String> addQueue(@Valid @RequestBody EventRequest eventRequest) {

    Event event = eventRequest.getEvent();
    if (event == null) {
      return ResponseEntity.badRequest().body("Event cannot be null.");
    }

    queueService.addEvent(event);
    return ResponseEntity.ok("Events successfully added to the queue.");
  }

  @GetMapping
  public ResponseEntity<String> displayPort() {
    String serverPort = env.getProperty("server.port", "Default port (not set)");
    return ResponseEntity.ok("Application is running on port: " + serverPort);
  }
}
