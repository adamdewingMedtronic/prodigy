package com.mdt.taymyr.dto.card;

import lombok.Data;

@Data
public class Link {
    private String label;       // REQUIRED - human-readable label to display
    private String url;         // REQUIRED - URL to GET when link is clicked
    private String type;        // REQUIRED - type of the given URL (absolute/smart)
    private String appContext;  // OPTIONAL - additional context to share with a linked SMART app
}
