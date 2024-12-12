package com.practice.domain.handlers;

import com.practice.domain.events.Event;

public interface Handler {

  void handle(Event event);
}
