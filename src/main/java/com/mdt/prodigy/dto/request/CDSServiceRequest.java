package com.mdt.prodigy.dto.request;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Bundle;

import java.util.Map;


@Data
@Slf4j
public class CDSServiceRequest {
    private String hook;                // REQUIRED - hook that triggered this CDS Service call
    private String hookInstance;        // REQUIRED - UUID for this hook call
    private String fhirServe;           // OPTIONAL - base URL for CDS Client’s FHIR server
    private FhirAuthorization fhirAuthorization;   // OPTIONAL - structure with FHIR Authorization information for the above url
    private Map<String, Object> context;             // REQUIRED - hook-specific contextual data
    private ProdigyPrefetchRequest prefetch;            // OPTIONAL - FHIR data prefetched by the CDS Client

}
