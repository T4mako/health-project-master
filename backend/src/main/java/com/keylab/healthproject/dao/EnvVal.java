package com.keylab.healthproject.dao;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvVal {

  private String id;
  private String deviceUuid;
  private double co2;
  private double tvoc;
  private double light;
  private double pm25;
  private double db;
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


  public double getCo2() {
    return co2;
  }

  public void setCo2(double co2) {
    this.co2 = co2;
  }


  public double getTvoc() {
    return tvoc;
  }

  public void setTvoc(double tvoc) {
    this.tvoc = tvoc;
  }


  public double getLight() {
    return light;
  }

  public void setLight(double light) {
    this.light = light;
  }


  public double getPm25() {
    return pm25;
  }

  public void setPm25(double pm25) {
    this.pm25 = pm25;
  }


  public double getDb() {
    return db;
  }

  public void setDb(double db) {
    this.db = db;
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
