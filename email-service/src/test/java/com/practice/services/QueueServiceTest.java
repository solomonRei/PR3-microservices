package com.practice.services;


import com.practice.events.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class QueueServiceTest {

    private QueueService queueService;

    @BeforeEach
    void setUp() {
        queueService = new QueueService();
    }

    @Test
    void addEvent_ShouldAddEventToQueue() {
        // Given
        Event mockEvent = mock(Event.class);

        // When
        queueService.addEvent(mockEvent);

        // Then
        assertTrue(queueService.getQueue().contains(mockEvent), "Queue should contain the added event.");
    }
}
