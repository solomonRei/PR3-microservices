package com.practice.domain.handlers.impl;

import com.google.gson.Gson;
import com.practice.config.EmailServiceProperties;
import com.practice.domain.events.Event;
import com.practice.domain.events.impl.MailEvent;
import com.practice.domain.handlers.Handler;
import com.practice.domain.model.EventModel;
import com.practice.domain.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * MailHandler
 */
@Slf4j
@Component
public class MailHandler implements Handler {

    private final EventRepository eventRepository;

    private final RestTemplate restTemplate;

    private final Gson gson = new Gson();

    private final EmailServiceProperties emailServiceProperties;

    @Autowired
    public MailHandler(EventRepository eventRepository, EmailServiceProperties emailServiceProperties) {
        this.eventRepository = eventRepository;
        this.restTemplate = new RestTemplate();
        this.emailServiceProperties = emailServiceProperties;
    }

    @Override
    public void handle(Event event) {
        MailEvent mailEvent = (MailEvent) event;
        log.info("Handling mail event: ");
        sendMail(mailEvent);
        EventModel eventModel = fromEventToModel(mailEvent);
        eventRepository.save(eventModel);
    }

    private void sendMail(MailEvent mailEvent) {
        HttpEntity<MailEvent> request = new HttpEntity<>(mailEvent);
        log.info("Sending email");
        log.info("Host: " + emailServiceProperties.getHost());

        try {

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "http://" + emailServiceProperties.getHost() + ":" + emailServiceProperties.getPort() + "/api/email/addQueue", request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully");
            } else {
                log.info("Failed to send email: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to send email: " + e.getMessage());
        }
    }

    private EventModel fromEventToModel(MailEvent event) {
        String jsonData = gson.toJson(event);

        EventModel eventModel = new EventModel();
        eventModel.setType("MailEvent");
        eventModel.setData(jsonData);
        return eventModel;
    }
}
