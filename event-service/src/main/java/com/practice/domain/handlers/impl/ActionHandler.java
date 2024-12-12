package com.practice.domain.handlers.impl;

import com.google.gson.Gson;
import com.practice.domain.handlers.Handler;
import com.practice.domain.model.EventModel;
import com.practice.domain.repository.EventRepository;
import com.practice.domain.events.Event;
import com.practice.domain.events.impl.ActionEvent;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ActionHandler class.
 */
@Component
public class ActionHandler implements Handler {

  private final EventRepository eventRepository;

  @Autowired
  public ActionHandler(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public void handle(Event event) {
    EventModel eventModel = fromEventToModel((ActionEvent) event);
    System.out.println(eventModel.getData());
    eventRepository.save(eventModel);
  }

  private EventModel fromEventToModel(ActionEvent event) {
    Gson gson = new Gson();

    Map<String, String> messageMap = new HashMap<>();
    messageMap.put("message", event.getMessage());

    String jsonData = gson.toJson(messageMap);
    EventModel model = new EventModel();
    model.setData(jsonData);
    model.setType("ActionEvent");

    return model;
  }
}
