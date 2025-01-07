package com.keylab.healthproject.task;

/**
 * @author T4mako
 * @date 2025/1/5 18:01
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keylab.healthproject.service.impl.HealthDataServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InsertDataJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(InsertDataJob.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HealthDataServiceImpl healthDataServiceImpl;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("健康数据插入...");
        File jsonFile = new File("all_records.json");

        if (!jsonFile.exists()) {
            log.error("找不到文件 all_records.json");
            return;
        }

        try {
            List<Map<String, Object>> records = objectMapper.readValue(jsonFile, List.class);
            healthDataServiceImpl.upsertHealthData(records);
        } catch (IOException e) {
            System.err.println("Error reading data from JSON file: " + e.getMessage());
        }
        log.info("健康数据已插入");
    }
}
