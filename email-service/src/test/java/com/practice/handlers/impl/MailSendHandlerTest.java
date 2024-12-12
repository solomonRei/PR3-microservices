package com.practice.handlers.impl;

import static org.mockito.Mockito.*;

import com.practice.events.impl.MailSendEvent;
import com.practice.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailSendHandlerTest {

  @Mock private EmailService emailService;

  @InjectMocks private MailSendHandler mailSendHandler;

  @Test
  void handle_ShouldInvokeEmailServiceWithMailSendEvent() {
    // Given
    MailSendEvent mailSendEvent = mock(MailSendEvent.class);

    // Act
    mailSendHandler.handle(mailSendEvent);

    // Then
    verify(emailService, times(1)).sendMessage(mailSendEvent);
  }
}
