package com.practice.domain.events.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.domain.events.Event;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Class for action event. */
@Getter
@Setter
public class ActionEvent implements Event {

  @JsonProperty("message")
  @NotNull(message = "Message cannot be null")
  private String message;

  @JsonProperty("action")
  @NotNull(message = "Action cannot be null")
  private String action;
}
