package com.practice.model;

import lombok.Getter;
import lombok.Setter;

/** Model for event. */
@Getter
@Setter
public class EventModel {

  private Long id;

  private String data;

  private String type;

  private boolean isProcessed;
}
