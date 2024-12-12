package com.practice.services;

import com.practice.handlers.HandlerFactory;
import com.practice.handlers.QueueHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service to manage the threads.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ThreadService {

  private final QueueService queueService;

  private final HandlerFactory handlerFactory;

  /**
   * Start the manager.
   *
   * @param nbOfThreads number of threads
   */
  public void startManager(int nbOfThreads) {
    final ExecutorService executorService = Executors.newFixedThreadPool(nbOfThreads);
    var queue = queueService.getQueue();
    QueueHandler queueHandler = new QueueHandler(queue, handlerFactory, executorService);

    queueHandler.processEvent();
  }
}
