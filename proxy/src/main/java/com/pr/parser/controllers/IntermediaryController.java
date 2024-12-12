package com.pr.parser.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/leader")
@Tag(name = "Currency API", description = "API for managing currencies")
@RequiredArgsConstructor
public class IntermediaryController {

    private volatile String currentLeader;
    private final WebClient webClient;

    @PostMapping(value = "/update")
    public ResponseEntity<String> updateLeader(@RequestBody Map<String, String> body) {
        this.currentLeader = body.get("leaderId");
        log.info("Leader updated to: {}", currentLeader);
        return ResponseEntity.ok("Leader updated to: " + currentLeader);
    }

    @GetMapping(value = "/current")
    public ResponseEntity<String> getCurrentLeader() {
        log.info("Current leader: {}", currentLeader);
        return ResponseEntity.ok(currentLeader != null ? currentLeader : "No leader");
    }

    @PostMapping(value = "/election/start")
    public ResponseEntity<String> startElection() {
        log.info("Starting election process");
        List<String> clusterNodes = List.of("http-server-1:8082", "http-server-2:8082", "http-server-3:8082");
        clusterNodes.forEach(node -> {
            String httpServerUrl = "http://" + node + "/election/start";
            webClient.post()
                    .uri(httpServerUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        });
        return ResponseEntity.ok("Election process proxied and started: ");
    }

}

