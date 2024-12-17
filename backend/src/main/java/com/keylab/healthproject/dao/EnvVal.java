package com.keylab.healthproject.dao;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvVal {

  private String id;
  private String deviceUuid;
  private double co;
  private double pressure;
  private String light;
  private double pm25;
  private double pm10;
  private double humidity;
  private double temperature;
  private long familyUserId;
  private long researchedPersonId;
  private long deptId;
  private String createTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDeviceUuid() {
    return deviceUuid;
  }

  public void setDeviceUuid(String deviceUuid) {
    this.deviceUuid = deviceUuid;
  }

  public double getCo() {
    return co;
  }

  public void setCo(double co) {
    this.co = co;
  }

  public double getPressure() {
    return pressure;
  }

  public void setPressure(double pressure) {
    this.pressure = pressure;
  }

  public String getLight() {
    return light;
  }

  public void setLight(String light) {
    this.light = light;
  }

  public double getPm25() {
    return pm25;
  }

  public void setPm25(double pm25) {
    this.pm25 = pm25;
  }

  public double getPm10() {
    return pm10;
  }

  public void setPm10(double pm10) {
    this.pm10 = pm10;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public long getFamilyUserId() {
    return familyUserId;
  }

  public void setFamilyUserId(long familyUserId) {
    this.familyUserId = familyUserId;
  }

  public long getResearchedPersonId() {
    return researchedPersonId;
  }

  public void setResearchedPersonId(long researchedPersonId) {
    this.researchedPersonId = researchedPersonId;
  }

  public long getDeptId() {
    return deptId;
  }

  public void setDeptId(long deptId) {
    this.deptId = deptId;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
}
