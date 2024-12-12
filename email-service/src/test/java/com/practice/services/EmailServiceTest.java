package com.practice.services;

import static org.mockito.Mockito.*;

import com.practice.events.impl.MailSendEvent;
import com.practice.model.EventModel;
import com.practice.repository.EventRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

  @Mock private EventRepository eventRepository;
  @Mock private QueueService queueService;
  @InjectMocks private EmailService emailService;

  @Test
  void processMessagesToQueue_ShouldProcessAndMarkAsProcessed() {
    // Given
    EventModel eventModel = new EventModel();
    eventModel.setId(1L);
    eventModel.setData(
        "{\"to\":\"example@example.com\",\"from\":\"noreply@example.com\",\"subject\":\"Test\",\"message\":\"Test message\"}");
    eventModel.setType("MailEvent");
    List<EventModel> unprocessedEvents = List.of(eventModel);

    when(eventRepository.getUnprocessed()).thenReturn(unprocessedEvents);

    // When
    emailService.processMessagesToQueue();

    // Then
    verify(queueService, times(1)).addEvent(any(MailSendEvent.class));
    verify(eventRepository, times(1)).markAsProcessed(1L);
  }
}
