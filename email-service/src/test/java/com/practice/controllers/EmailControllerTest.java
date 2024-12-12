package com.practice.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.practice.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private EmailService emailService;

  @Test
  void emailQueue_ShouldProcessMessagesAndReturnSuccess() throws Exception {
    mockMvc
        .perform(post("/emailQueue").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Events successfully added to the queue."));

    verify(emailService).processMessagesToQueue();
  }
}
