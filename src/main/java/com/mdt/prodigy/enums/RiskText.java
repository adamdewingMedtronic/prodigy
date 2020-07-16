package com.mdt.prodigy.enums;

import lombok.Getter;

@Getter
public enum RiskText {
    HIGH("High Risk", "red"),
    INTERMEDIATE("Intermediate Risk", "yellow"),
    LOW("Low Risk", "green");

    private String color;
    private String text;

    private RiskText(String text, String color){
        this.text = text;
        this.color = color;
    }
}
