package com.practice.events.impl;

import com.practice.events.Event;
import lombok.Getter;
import lombok.Setter;

/** Represents an event that can be triggered. */
@Getter
@Setter
public class MailSendEvent implements Event {

  private String message;

  private String subject;

  private String to;

  private String from;
}
