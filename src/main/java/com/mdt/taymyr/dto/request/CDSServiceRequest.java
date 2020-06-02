package com.mdt.taymyr.dto.request;

public class CDSServiceRequest {
    private String hook;                // REQUIRED - hook that triggered this CDS Service call
    private String hookInstance;        // REQUIRED - UUID for this hook call
    private String fhirServe;           // OPTIONAL - base URL for CDS Client’s FHIR server
    private FhirAuthorization fhirAuthorization;   // OPTIONAL - structure with FHIR Authorization information for the above url
    private String context;             // REQUIRED - hook-specific contextual data
    private String prefetch;            // OPTIONAL - FHIR data prefetched by the CDS Client
}
