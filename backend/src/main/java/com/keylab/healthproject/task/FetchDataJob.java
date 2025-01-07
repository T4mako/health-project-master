package com.keylab.healthproject.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keylab.healthproject.service.impl.HealthDataServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author T4mako
 * @date 2025/1/5 18:00
 */
public class FetchDataJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(FetchDataJob.class);

    @Value("${health.api.url}")
    private String apiUrl;

    @Value("${health.api.token}")
    private String apiToken;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
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
        log.info("健康数据已查询");
    }
}
