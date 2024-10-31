package com.keylab.healthproject.common;

import java.util.HashSet;

public class Indicator {
    // 静态 HashSet
    public static final HashSet<String> healthIndicators = new HashSet<>();

    static {
        healthIndicators.add("breath_rate");
        healthIndicators.add("systolic");
        healthIndicators.add("diastolic");
        healthIndicators.add("blood_oxygen");
        healthIndicators.add("temperature");
        healthIndicators.add("heart_rate");
        healthIndicators.add("blood_glucose");
    }
}
