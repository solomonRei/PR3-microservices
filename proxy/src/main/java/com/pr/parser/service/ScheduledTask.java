package com.pr.parser.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private final AsyncFtpFileService ftpFileService;

    public ScheduledTask(AsyncFtpFileService ftpFileService) {
        this.ftpFileService = ftpFileService;
    }

    @Scheduled(fixedRate = 10000)
    public void triggerAsyncFtpFetch() {
        ftpFileService.fetchFileFromFTP();
    }
}

