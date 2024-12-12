package com.practice.domain.services;

import com.practice.domain.events.Event;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Service for queue.
 */
@Getter
@Setter
@Service
public class QueueService {

  private ConcurrentLinkedQueue<Event> queue = new ConcurrentLinkedQueue<>();

  public void addEvent(Event event) {
    queue.add(event);
  }
}
