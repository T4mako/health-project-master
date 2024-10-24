package com.keylab.healthproject.dao;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

@TableName("community")
public class Community {

  private long id;
  private long depId;
  private String name;
  private String total;
  private String female;
  private String male;
  @JsonProperty("family_num")
  private String familyNum;
  @TableField("age_30")
  @JsonProperty("30-")
  private String age_30_; // 设置字段映射
  @TableField("age_30_40")
  @JsonProperty("30-40")
  private String age_30_40;
  @TableField("age_40_50")
  @JsonProperty("40-50")
  private String age_40_50;
  @TableField("age_50_60")
  @JsonProperty("50-60")
  private String age_50_60;
  @TableField("age_60_70")
  @JsonProperty("60-70")
  private String age_60_70;
  @TableField("age_70")
  @JsonProperty("70+")
  private String age_70;
  private String normal;
  @JsonProperty("warn_level1")
  private long warnLevel1;
  @JsonProperty("warn_level2")
  private long warnLevel2;
  @JsonProperty("warn_level3")
  private long warnLevel3;
  private java.sql.Timestamp createTime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getDepId() {
    return depId;
  }

  public void setDepId(long depId) {
    this.depId = depId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getFemale() {
    return female;
  }

  public void setFemale(String female) {
    this.female = female;
  }

  public String getMale() {
    return male;
  }

  public void setMale(String male) {
    this.male = male;
  }

  public String getFamilyNum() {
    return familyNum;
  }

  public void setFamilyNum(String familyNum) {
    this.familyNum = familyNum;
  }

  public String getAge_30_() {
    return age_30_;
  }

  public void setAge_30_(String age_30_) {
    this.age_30_ = age_30_;
  }

  public String getAge_30_40() {
    return age_30_40;
  }

  public void setAge_30_40(String age_30_40) {
    this.age_30_40 = age_30_40;
  }

  public String getAge_40_50() {
    return age_40_50;
  }

  public void setAge_40_50(String age_40_50) {
    this.age_40_50 = age_40_50;
  }

  public String getAge_50_60() {
    return age_50_60;
  }

  public void setAge_50_60(String age_50_60) {
    this.age_50_60 = age_50_60;
  }

  public String getAge_60_70() {
    return age_60_70;
  }

  public void setAge_60_70(String age_60_70) {
    this.age_60_70 = age_60_70;
  }

  public String getAge_70() {
    return age_70;
  }

  public void setAge_70(String age_70) {
    this.age_70 = age_70;
  }

  public String getNormal() {
    return normal;
  }

  public void setNormal(String normal) {
    this.normal = normal;
  }

  public long getWarnLevel1() {
    return warnLevel1;
  }

  public void setWarnLevel1(long warnLevel1) {
    this.warnLevel1 = warnLevel1;
  }

  public long getWarnLevel2() {
    return warnLevel2;
  }

  public void setWarnLevel2(long warnLevel2) {
    this.warnLevel2 = warnLevel2;
  }

  public long getWarnLevel3() {
    return warnLevel3;
  }

  public void setWarnLevel3(long warnLevel3) {
    this.warnLevel3 = warnLevel3;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }
}