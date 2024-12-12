package com.practice.events.impl;

import com.practice.events.Event;
import lombok.Getter;
import lombok.Setter;

/** Represents an event that can be triggered. */
@Getter
@Setter
public class EventWithId implements Event {
  private final Long id;
  private final MailSendEvent event;

  public EventWithId(Long id, MailSendEvent event) {
    this.id = id;
    this.event = event;
  }
}
