package com.test.margorecruitassigment.controller;

import com.test.margorecruitassigment.service.EventLoggerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.status;

@AllArgsConstructor
@RestController
@RequestMapping("/api/loadEvents")
public class EventLoggerController {
    private final EventLoggerService eventLoggerService;

    @PostMapping
    public ResponseEntity<Object> loadEventsFromTxtFile(@RequestParam("file") MultipartFile file) throws Exception {
        Integer numberOfLogs = eventLoggerService.loadEventsFromFile(file);
        return status(HttpStatus.OK).body(numberOfLogs + " new logs data has been added to event Table!");
    }
}
