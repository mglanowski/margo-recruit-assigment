package com.test.margorecruitassigment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.margorecruitassigment.model.AppEvent;
import com.test.margorecruitassigment.model.Log;
import com.test.margorecruitassigment.model.State;
import com.test.margorecruitassigment.repository.EventLoggerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@AllArgsConstructor
public class EventLoggerService {

    private EventLoggerRepository eventLoggerRepository;
    private static Logger logger = LoggerFactory.getLogger(EventLoggerService.class);

    public Integer loadEventsFromFile(MultipartFile file) throws Exception {

        List<Log> logs = readLogsFromFile(file);
        logger.info("The data has been read from the file by: " + Thread.currentThread());

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Map<String, AppEvent> logMap = new HashMap<>();
        for (int i = 0; i < logs.size(); i++) {
            int finalI = i;
            executorService.submit(() -> {
                        processEvent(logMap, logs.get(finalI));
                        logger.info("Event number " + finalI + " has been processed by " + Thread.currentThread());
                    }
            );
        }
        logger.info("The data has been read from the file!");
        eventLoggerRepository.saveAll(logMap.values());
        return logs.size();
    }

    public List<Log> readLogsFromFile(MultipartFile file) throws Exception {
        List<Log> list = new ArrayList<>();
        InputStream inputStream = null;
        Scanner sc = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            inputStream = file.getInputStream();
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                list.add(objectMapper.readValue(sc.nextLine(), Log.class));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        return list;
    }

    public void processEvent(Map<String, AppEvent> logMap, Log log) {
        if (logMap.containsKey(log.getId()) && log.getState().equals(State.STARTED)) {
            AppEvent appEvent = logMap.get(log.getId());
            appEvent.setEventDuration(appEvent.getEndTime().getTime() - log.getTimestamp().getTime());
            if (appEvent.getEventDuration() > 4L) {
                appEvent.setAlert(true);
            }
        } else if (logMap.containsKey(log.getId()) && log.getState().equals(State.FINISHED)) {
            AppEvent appEvent = logMap.get(log.getId());
            appEvent.setEventDuration(log.getTimestamp().getTime() - appEvent.getStartTime().getTime());
            if (appEvent.getEventDuration() > 4L) {
                appEvent.setAlert(true);
            }
        } else if (log.getState().equals(State.STARTED)) {
            logMap.put(log.getId(), new AppEvent(log.getId(), log.getTimestamp(), null, 0L, log.getType(), log.getHost(), false));
        } else if (log.getState().equals(State.FINISHED)) {
            logMap.put(log.getId(), new AppEvent(log.getId(), null, log.getTimestamp(), 0L, log.getType(), log.getHost(), false));
        }
    }
}