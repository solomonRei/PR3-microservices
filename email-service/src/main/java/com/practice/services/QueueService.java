package com.practice.services;

import com.practice.events.Event;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/** Service for queue. */
@Getter
@Setter
@Service
public class QueueService {

  private ConcurrentLinkedQueue<Event> queue = new ConcurrentLinkedQueue<>();

  public void addEvent(Event event) {
    queue.add(event);
  }
}
