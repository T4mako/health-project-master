package com.keylab.healthproject.task;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.keylab.healthproject.dao.EnvVal;
import com.keylab.healthproject.service.IEnvValService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 定时任务：采集空气质量数据
 * 每小时执行一次并将数据保存到数据库
 */
@Slf4j
@Component
@EnableScheduling
public class AirQualityTask {

    @Autowired
    private IEnvValService envValService;

    // API key 和基础 URL
    private static final String API_KEY = "065a7eca4fe849c4931bbdaa90682926";
    private static final String WEATHER_URL = "https://devapi.qweather.com/v7/weather/now";
    private static final String AIR_URL = "https://devapi.qweather.com/v7/air/now";

    // 城市信息
    private static final List<Map<String, Object>> CITIES = Arrays.asList(
            Map.of("cityId", "101190801", "cityName", "徐州", "deptId", 10),
            Map.of("cityId", "101180101", "cityName", "郑州", "deptId", 11),
            Map.of("cityId", "101110101", "cityName", "西安", "deptId", 12)
    );

    @Scheduled(cron = "0 0 * * * ?") // 每小时执行一次
    public void fetchAndInsertData() {
        log.info("开始采集空气质量数据...");

        for (Map<String, Object> city : CITIES) {
            String cityId = city.get("cityId").toString();
            String cityName = city.get("cityName").toString();
            Integer deptId = (Integer) city.get("deptId");

            try {
                // 构建请求 URL
                String weatherUrl = String.format("%s?location=%s&key=%s", WEATHER_URL, cityId, API_KEY);
                String airUrl = String.format("%s?location=%s&key=%s", AIR_URL, cityId, API_KEY);

                // 发送 HTTP 请求
                HttpResponse weatherResponse = HttpUtil.createGet(weatherUrl)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .execute();

                HttpResponse airResponse = HttpUtil.createGet(airUrl)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .execute();

                // 检查响应状态
                if (weatherResponse.getStatus() == 200 && airResponse.getStatus() == 200) {
                    Map<String, Object> weatherData = JSONUtil.parseObj(weatherResponse.body());
                    Map<String, Object> airData = JSONUtil.parseObj(airResponse.body());

                    if ("200".equals(weatherData.get("code")) && "200".equals(airData.get("code"))) {
                        Map nowWeather = (Map) weatherData.get("now");
                        Map nowAir = (Map) airData.get("now");

                        // 构建 EnvVal 对象
                        EnvVal envVal = new EnvVal();
                        envVal.setCo(Double.parseDouble(nowAir.get("co").toString()));
                        envVal.setPressure(Double.parseDouble(nowWeather.get("pressure").toString()));
                        envVal.setLight(nowWeather.get("text").toString());
                        envVal.setPm25(Double.parseDouble(nowAir.get("pm2p5").toString()));
                        envVal.setPm10(Double.parseDouble(nowAir.get("pm10").toString()));
                        envVal.setHumidity(Double.parseDouble(nowWeather.get("humidity").toString()));
                        envVal.setTemperature(Double.parseDouble(nowWeather.get("temp").toString()));
                        envVal.setDeptId(deptId);

                        // 插入数据到数据库
                        envValService.saveEnvVal(envVal);
                        log.info("{} 数据已成功插入数据库！", cityName);
                    } else {
                        log.warn("{} 数据获取失败，返回状态码不为 200", cityName);
                    }
                } else {
                    log.warn("{} 请求失败，HTTP 状态码: {}, {}", cityName, weatherResponse.getStatus(), airResponse.getStatus());
                }
            } catch (Exception e) {
                log.error("采集 {} 数据时发生错误: {}", cityName, e.getMessage(), e);
            }
        }

        log.info("空气质量数据采集任务完成！");
    }
}
