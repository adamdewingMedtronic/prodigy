package com.mdt.prodigy.enums;

import lombok.Getter;

public enum Type {
    OPIOD_TYPE("OPIOD"),
    SLEEP_DISORDER_TYPE("SD"),
    CHRONIC_HEART_FAILURE_TYPE("CHF");

    @Getter
    private String description;

    private Type(String description){
        this.description = description;
    }
}
