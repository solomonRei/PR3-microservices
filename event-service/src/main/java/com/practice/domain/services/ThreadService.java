package com.practice.domain.services;

import com.practice.domain.handlers.HandlerFactory;
import com.practice.domain.handlers.QueueHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service for managing threads.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ThreadService {

  private final QueueService queueService;

  private final HandlerFactory handlerFactory;

  /**
   * Start manager.
   *
   * @param nbOfThreads number of threads
   */
  public void startManager(int nbOfThreads) {
    ExecutorService executorService = Executors.newFixedThreadPool(nbOfThreads);
    var queue = queueService.getQueue();
    QueueHandler queueHandler = new QueueHandler(queue, handlerFactory, executorService);

    queueHandler.processEvent();
  }
}
