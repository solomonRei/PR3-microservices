package com.practice.domain.handlers;

import com.practice.domain.handlers.impl.ActionHandler;
import com.practice.domain.events.Event;
import com.practice.domain.events.impl.ActionEvent;
import com.practice.domain.events.impl.MailEvent;
import com.practice.domain.handlers.impl.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
/**
 * Factory for creating handlers.
 */
@Component
@RequiredArgsConstructor
public class HandlerFactory {

  private final ActionHandler actionHandler;

  private final MailHandler mailHandler;

  public Handler getHandler(Event event) {
    if (event instanceof ActionEvent) {
      return actionHandler;
    } else if (event instanceof MailEvent) {
      return mailHandler;
    }
    throw new IllegalArgumentException("Unknown event type");
  }
}
