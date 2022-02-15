package com.test.margorecruitassigment.service;

import com.test.margorecruitassigment.model.AppEvent;
import com.test.margorecruitassigment.model.Log;
import com.test.margorecruitassigment.model.State;
import com.test.margorecruitassigment.repository.EventLoggerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EventLoggerServiceTest {

    @Mock
    private EventLoggerRepository eventLoggerRepository;

    private EventLoggerService eventLoggerService;

    @BeforeEach
    void setUp() {
        eventLoggerService = new EventLoggerService(eventLoggerRepository);
    }

    @Test
    void loadEventsFromFile() {
    }

    @Test
    void readLogsFromFile() throws Exception {
        Log log1 = new Log("scsmbstgra", State.STARTED, Date.from(Instant.ofEpochMilli(1491377495212L)), "APPLICATION_LOG", "12345");
        Log log2 = new Log("scsmbstgrb", State.STARTED, Date.from(Instant.ofEpochMilli(1491377495213L)), "APPLICATION_LOG", "12345");
        List<Log> expectedList = new ArrayList<>();
        expectedList.add(log1);
        expectedList.add(log2);

        MultipartFile multipartFile = new MockMultipartFile("logfile.txt", new FileInputStream(new File("src\\test\\resources\\logfile.txt")));

        //when
        List<Log> logs = eventLoggerService.readLogsFromFile(multipartFile);

        //then
        assertEquals(expectedList.size(), logs.size());
    }

    @Test
    void processNewStartedEvent() {
        Map<String, AppEvent> logMap = new HashMap<>();
        Log log1 = new Log("scsmbstgra", State.STARTED, Date.from(Instant.ofEpochMilli(1491377495212L)), "APPLICATION_LOG", "12345");

        //when
        eventLoggerService.processEvent(logMap, log1);

        //then
        assertEquals(1, logMap.size());
        assertEquals(Date.from(Instant.ofEpochMilli(1491377495212L)), logMap.get("scsmbstgra").getStartTime());
        assertEquals(null, logMap.get("scsmbstgra").getEndTime());
    }

    @Test
    void processNewFinishedEvent() {
        Map<String, AppEvent> logMap = new HashMap<>();
        Log log1 = new Log("scsmbstgra", State.FINISHED, Date.from(Instant.ofEpochMilli(1491377495212L)), null, null);

        //when
        eventLoggerService.processEvent(logMap, log1);

        //then
        assertEquals(1, logMap.size());
        assertEquals(Date.from(Instant.ofEpochMilli(1491377495212L)), logMap.get("scsmbstgra").getEndTime());
        assertEquals(null, logMap.get("scsmbstgra").getStartTime());
    }

    @Test
    void processActualStartedEvent() {
        Map<String, AppEvent> logMap = new HashMap<>();
        Log log1 = new Log("scsmbstgra", State.STARTED, Date.from(Instant.ofEpochMilli(1491377495201L)), null, null);
        logMap.put(log1.getId(), new AppEvent(log1.getId(), log1.getTimestamp(), null, null, log1.getType(), log1.getHost(), false));

        Log log2 = new Log("scsmbstgra", State.FINISHED, Date.from(Instant.ofEpochMilli(1491377495212L)), null, null);
        //when
        eventLoggerService.processEvent(logMap, log2);

        //then
        assertEquals(1, logMap.size());
        assertEquals(1491377495212L - 1491377495201L, logMap.get("scsmbstgra").getEventDuration());
        assertEquals(true, logMap.get("scsmbstgra").getAlert());
    }

    @Test
    void processActualFinishedEvent() {
        Map<String, AppEvent> logMap = new HashMap<>();
        Log log1 = new Log("scsmbstgra", State.FINISHED, Date.from(Instant.ofEpochMilli(1491377495212L)), null, null);
        logMap.put(log1.getId(), new AppEvent(log1.getId(), null, log1.getTimestamp(), null, log1.getType(), log1.getHost(), false));

        Log log2 = new Log("scsmbstgra", State.STARTED, Date.from(Instant.ofEpochMilli(1491377495201L)), null, null);
        //when
        eventLoggerService.processEvent(logMap, log2);

        //then
        assertEquals(1, logMap.size());
        assertEquals(1491377495212L - 1491377495201L, logMap.get("scsmbstgra").getEventDuration());
        assertEquals(true, logMap.get("scsmbstgra").getAlert());
    }
}