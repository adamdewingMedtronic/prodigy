package com.mdt.taymyr.dto.discovery;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class CDSService {

    private String hook;            // REQUIRED - hook this service should be invoked on
    private String title;           // RECOMMENDED - human-friendly name of this service
    private String description;     // REQUIRED - description of this service
    private String id;              // REQUIRED - {id} portion of service URL: {baseUrl}/cds-services/{id}
    private Map<String, String> prefetch = new HashMap<>(); // OPTIONAL - Object containing key/value pairs of FHIR queries that this service is requesting the CDS Client to include on service calls

}