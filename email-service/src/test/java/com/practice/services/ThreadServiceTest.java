package com.practice.services;

import static org.mockito.Mockito.*;

import com.practice.events.Event;
import com.practice.handlers.HandlerFactory;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ThreadServiceTest {

  @Mock private QueueService queueService;

  @Mock private HandlerFactory handlerFactory;

  @InjectMocks private ThreadService threadService;

  @Test
  void startManager_ShouldInvokeProcessEvent() {
    // Given
    when(queueService.getQueue()).thenReturn(new ConcurrentLinkedQueue<Event>());

    // Act
    threadService.startManager(5);

    // Assert
    verify(queueService, times(1)).getQueue();
  }
}
