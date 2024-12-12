package com.practice.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.practice.domain.events.impl.ActionEvent;
import com.practice.domain.events.impl.MailEvent;

/** Interface for Event. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ActionEvent.class, name = "actionEvent"),
  @JsonSubTypes.Type(value = MailEvent.class, name = "mailEvent")
})
public interface Event {}
