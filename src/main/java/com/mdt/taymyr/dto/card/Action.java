package com.mdt.taymyr.dto.card;

import lombok.Data;

@Data
public class Action {
    private String type;            // REQUIRED - type of action being performed (create/update/delete)
    private String description;     // REQUIRED - human-readable description of the suggested action
    private String resource;        // OPTIONAL - FHIR resource to create/update or id of resource to delete
}
