package com.practice.services;

import com.google.gson.Gson;
import com.practice.config.properties.EmailDefaultProperties;
import com.practice.events.impl.EventWithId;
import com.practice.events.impl.MailSendEvent;
import com.practice.repository.EventRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/** Service for sending emails. */
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  private final TemplateEngine templateEngine;

  private final EventRepository eventRepository;

  private final QueueService queueService;

  private final EmailDefaultProperties emailDefaultProperties;

  private final Gson gson = new Gson();


  /** Process messages to queue. */
  public void processMessagesToQueue() {
    List<EventWithId> unprocessedEventsWithId =
        eventRepository.getUnprocessed().stream()
            .map(
                event ->
                    new EventWithId(
                        event.getId(), gson.fromJson(event.getData(), MailSendEvent.class)))
            .toList();

    unprocessedEventsWithId.forEach(
        eventWithId -> {
          queueService.addEvent(eventWithId.getEvent());
          eventRepository.markAsProcessed(eventWithId.getId());
        });
  }

  /**
   * Send message.
   *
   * @param event the event
   */
  @Retryable(value = MessagingException.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public void sendMessage(MailSendEvent event) {
    try {

      MimeMessage msg = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true);

      if (event.getFrom() == null || event.getFrom().isEmpty()) {
        event.setFrom(emailDefaultProperties.getDefaultFrom());
      }
      helper.setFrom(event.getFrom());
      helper.setTo(event.getTo());
      msg.setSubject(event.getSubject());

      Context context = new Context();
      context.setVariable("message", event.getMessage());

      String htmlContent = templateEngine.process("emailTemplate", context);

      helper.setText(htmlContent, true);
      javaMailSender.send(msg);
    } catch (MessagingException e) {
      System.err.println("Exception while sending email: " + e.getMessage());
    }
  }

  /**
   * Recover.
   *
   * @param e the e
   * @param event the event
   */
  @Recover
  public void recover(MessagingException e, MailSendEvent event) {
    System.err.println("Failed to send email after retries, giving up.");
  }
}
