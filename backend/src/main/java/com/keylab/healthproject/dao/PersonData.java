package com.keylab.healthproject.dao;

import lombok.Data;

/**
 * @author T4mako
 * @date 2024/10/29 15:53
 */
@Data
public class PersonData {
    private long id;
    private String gender;
    private long age;
    private long familyUserId;
    private long deptId;
    private String deptName;
    private double height;
    private double weight;
    private double bmi;
}
