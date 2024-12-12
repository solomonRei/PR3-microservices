package com.practice.domain.model;

import lombok.Getter;
import lombok.Setter;

/** Event model. */
@Getter
@Setter
public class EventModel {

  private Long id;

  private String data;

  private String type;

  private boolean isProcessed;
}
