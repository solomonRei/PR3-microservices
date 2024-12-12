package com.practice.domain.events.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.domain.events.Event;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Represents an event that can be triggered. */
@Getter
@Setter
public class MailEvent implements Event {

  @JsonProperty("message")
  @NotNull(message = "Message cannot be null")
  private String message;

  @JsonProperty("subject")
  @NotNull(message = "Subject cannot be null")
  private String subject;

  @JsonProperty("to")
  @NotNull(message = "To cannot be null")
  private String to;

  @JsonProperty("from")
  private String from;
}
