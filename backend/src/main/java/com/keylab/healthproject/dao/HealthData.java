package com.keylab.healthproject.dao;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class HealthData {

  private long id;
  @JsonProperty("breath_rate")
  private long breathRate;
  private long systolic;
  private long diastolic;
  @JsonProperty("create_time")
  private LocalDate createTime;
  @JsonProperty("blood_oxygen")
  private long bloodOxygen;
  @JsonProperty("family_user_id")
  private String familyUserId;
  private double temperature;
  @JsonProperty("researched_person_id")
  private String researchedPersonId;
  @JsonProperty("heart_rate")
  private long heartRate;
  @JsonProperty("blood_glucose")
  private double bloodGlucose;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getBreathRate() {
    return breathRate;
  }

  public void setBreathRate(long breathRate) {
    this.breathRate = breathRate;
  }


  public long getSystolic() {
    return systolic;
  }

  public void setSystolic(long systolic) {
    this.systolic = systolic;
  }


  public long getDiastolic() {
    return diastolic;
  }

  public void setDiastolic(long diastolic) {
    this.diastolic = diastolic;
  }


  public LocalDate getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDate createTime) {
    this.createTime = createTime;
  }


  public long getBloodOxygen() {
    return bloodOxygen;
  }

  public void setBloodOxygen(long bloodOxygen) {
    this.bloodOxygen = bloodOxygen;
  }


  public String getFamilyUserId() {
    return familyUserId;
  }

  public void setFamilyUserId(String familyUserId) {
    this.familyUserId = familyUserId;
  }


  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }


  public String getResearchedPersonId() {
    return researchedPersonId;
  }

  public void setResearchedPersonId(String researchedPersonId) {
    this.researchedPersonId = researchedPersonId;
  }


  public long getHeartRate() {
    return heartRate;
  }

  public void setHeartRate(long heartRate) {
    this.heartRate = heartRate;
  }


  public double getBloodGlucose() {
    return bloodGlucose;
  }

  public void setBloodGlucose(double bloodGlucose) {
    this.bloodGlucose = bloodGlucose;
  }

}
