package com.mdt.prodigy.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Data;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.Map;

@Data
public class ProdigyPrefetchRequest {

    private JsonNode patient;
    private JsonNode medications;
    private JsonNode conditions;
    private JsonNode conditionsEnc;

}
