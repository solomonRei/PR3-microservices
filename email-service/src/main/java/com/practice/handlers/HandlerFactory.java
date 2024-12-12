package com.practice.handlers;

import com.practice.events.Event;
import com.practice.events.impl.MailSendEvent;
import com.practice.handlers.impl.MailSendHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Factory for creating handlers. */
@Component
@RequiredArgsConstructor
public class HandlerFactory {

  private final MailSendHandler mailSentHandler;

  public Handler getHandler(Event event) {
    if (event instanceof MailSendEvent) {
      return mailSentHandler;
    }
    throw new IllegalArgumentException("Unknown event type");
  }
}
