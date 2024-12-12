package com.practice.api.dto;

import com.practice.domain.events.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request for an event.
 */
@Getter
@Setter
public class EventRequest {

  private Event event;
}
