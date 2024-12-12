package com.practice.handlers;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.practice.events.Event;
import com.practice.events.impl.MailSendEvent;
import com.practice.handlers.impl.MailSendHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HandlerFactoryTest {

  @Mock private MailSendHandler mailSendHandler;

  @InjectMocks private HandlerFactory handlerFactory;

  @Test
  void getHandler_WithMailSendEvent_ShouldReturnMailSendHandler() {
    // Given
    MailSendEvent mailSendEvent = mock(MailSendEvent.class);

    // When
    Handler handler = handlerFactory.getHandler(mailSendEvent);

    // Then
    assertInstanceOf(
        MailSendHandler.class, handler, "Handler should be an instance of MailSendHandler.");
  }

  @Test
  void getHandler_WithUnknownEvent_ShouldThrowIllegalArgumentException() {
    // Given
    Event unknownEvent = mock(Event.class);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> handlerFactory.getHandler(unknownEvent),
        "Should throw IllegalArgumentException for unknown event types.");
  }
}
