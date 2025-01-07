package com.keylab.healthproject.config;

import com.keylab.healthproject.task.AirQualityJob;
import com.keylab.healthproject.task.FetchDataJob;
import com.keylab.healthproject.task.InsertDataJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author T4mako
 * @date 2025/1/5 17:59
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail fetchDataJobDetail() {
        return JobBuilder.newJob(FetchDataJob.class)
                .withIdentity("fetchDataJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger fetchDataJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(fetchDataJobDetail())
                .withIdentity("fetchDataTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 30 0 * * ?"))
                .build();
    }

    @Bean
    public JobDetail insertDataJobDetail() {
        return JobBuilder.newJob(InsertDataJob.class)
                .withIdentity("insertDataJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger insertDataJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(insertDataJobDetail())
                .withIdentity("insertDataTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 30 1 * * ?"))
                .build();
    }

    @Bean
    public JobDetail airQualityJobDetail() {
        return JobBuilder.newJob(AirQualityJob.class)
                .withIdentity("airQualityJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger airQualityJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(airQualityJobDetail())
                .withIdentity("airQualityTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?")) // 每小时执行一次
                .build();
    }
}
