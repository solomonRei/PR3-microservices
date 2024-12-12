package com.practice.services;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * ListenerService.
 */
@Slf4j
@Service
public class ListenerService {

  private final QueueService queueService;

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  private final ThreadService threadService;

  @Autowired
  public ListenerService(QueueService queueService, ThreadService threadService) {
    this.queueService = queueService;
    this.threadService = threadService;
  }

  @PostConstruct
  public void init() {
    executorService.submit(this::processEvents);
  }

  @Scheduled(fixedRate = 100)
  public void processEvents() {
    if (!queueService.getQueue().isEmpty()) {
      log.info("Processing event");
      threadService.startManager(5);
    }
  }
}
