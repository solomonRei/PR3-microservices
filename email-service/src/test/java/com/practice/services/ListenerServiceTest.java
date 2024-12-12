package com.practice.services;

import static org.mockito.Mockito.*;

import com.practice.events.Event;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListenerServiceTest {

  @Mock private QueueService queueService;

  @Mock private ThreadService threadService;

  @InjectMocks private ListenerService listenerService;

  @Test
  void whenQueueNotEmpty_shouldStartManager() {
    // Given
    when(queueService.getQueue())
        .thenReturn(new ConcurrentLinkedQueue<>(List.of(mock(Event.class))));

    // Act
    listenerService.processEvents();

    // Then
    verify(threadService, times(1)).startManager(5);
  }

  @Test
  void whenQueueIsEmpty_shouldNotStartManager() {
    // Given
    when(queueService.getQueue()).thenReturn(new ConcurrentLinkedQueue<>());

    // Act
    listenerService.processEvents();

    // Then
    verify(threadService, never()).startManager(anyInt());
  }
}
