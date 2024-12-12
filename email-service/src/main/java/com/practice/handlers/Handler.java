package com.practice.handlers;

import com.practice.events.Event;

/** Represents a handler that can handle an event. */
public interface Handler {

  void handle(Event event);
}
