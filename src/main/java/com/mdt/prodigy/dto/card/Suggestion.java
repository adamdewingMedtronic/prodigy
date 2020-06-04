package com.mdt.prodigy.dto.card;

import lombok.Data;

@Data
public class Suggestion {
    private String label;   // REQUIRED - human-readable label to display for this suggestion
    private String uuid;    // OPTIONAL - unique identifier for auditing and logging suggestions
    private String actions; // OPTIONAL - array of suggested Actions (logically AND’d together)
}
