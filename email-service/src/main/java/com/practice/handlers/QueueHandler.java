package com.practice.handlers;

import com.practice.events.Event;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;

/**
 * Queue handler.
 *
 * Class for handling events from queue.
 */
@Slf4j
public class QueueHandler {

  private final ConcurrentLinkedQueue<Event> queue;

  private final HandlerFactory handlerFactory;

  private final ExecutorService executorService;

  public QueueHandler(
      Queue<Event> queue, HandlerFactory handlerFactory, ExecutorService executorService) {
    this.queue = (ConcurrentLinkedQueue<Event>) queue;
    this.handlerFactory = handlerFactory;
    this.executorService = executorService;
  }

  /**
   * Process event.
   */
  public void processEvent() {
    CompletableFuture.supplyAsync(this::getEvent, executorService)
        .thenAcceptAsync(
            event -> {
              if (event != null) {
                callHandler(event);
              }
            },
            executorService)
        .thenRunAsync(this::processEvent, executorService);
  }

  public Event getEvent() {
    return queue.poll();
  }

  /**
   * Call handler.
   *
   * @param event event
   */
  private void callHandler(Event event) {
    Handler handler = handlerFactory.getHandler(event);
    handler.handle(event);
  }
}
