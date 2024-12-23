package com.keylab.healthproject.task;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * @author T4mako
 * @date 2024/12/20 9:39
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Component
@EnableScheduling
public class HealthDataTasks {

    @Value("${health.api.url}")
    private String apiUrl;

    @Value("${health.api.token}")
    private String apiToken;

    private final DatabaseService databaseService;
    private final ObjectMapper objectMapper;

    public ScheduledTasks(DatabaseService databaseService, ObjectMapper objectMapper) {
        this.databaseService = databaseService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchDataTask() {
        List<String> types = Arrays.asList("temperature", "heartRate", "breathRate", "bloodOxygen", "bloodGlucose", "bloodPressure");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();
        Date endDate = calendar.getTime(); // 开始和结束时间都设为昨天

        List<Map<String, Object>> allRecords = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        for (String dataType : types) {
            String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate);

            Map<String, Object> payload = new HashMap<>();
            payload.put("current", 1);
            payload.put("size", 100);
            payload.put("startDate", dateStr);
            payload.put("endDate", dateStr);
            payload.put("type", dataType);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
                if (response.getBody() != null && response.getBody().get("code").equals(0)) {
                    List<Map<String, Object>> records = (List<Map<String, Object>>) ((Map) response.getBody().get("data")).get("records");
                    if (records != null) {
                        allRecords.addAll(records);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching data for type: " + dataType + " date: " + dateStr + ", error: " + e.getMessage());
            }
        }

        try (FileWriter file = new FileWriter("all_records.json")) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, allRecords);
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void insertDataTask() {
        File jsonFile = new File("all_records.json");

        if (!jsonFile.exists()) {
            System.err.println("JSON file not found.");
            return;
        }

        try {
            List<Map<String, Object>> records = objectMapper.readValue(jsonFile, List.class);
            databaseService.upsertHealthData(records);
        } catch (IOException e) {
            System.err.println("Error reading data from JSON file: " + e.getMessage());
        }
    }
}
