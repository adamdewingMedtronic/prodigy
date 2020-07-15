package com.mdt.prodigy.service;

import com.mdt.prodigy.dto.Risk;
import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.request.CDSServiceRequest;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.List;

public interface IProdigyService {


    Cards getORIDCards(CDSServiceRequest request);

    /**
     * Calculates the risk of OIRD (Opiod Induced Respiratory Depression) for a patient.
     * @param patient
     * @param medications
     * @param conditions
     * @param conditionsEnc
     * @return A list of risks with their type and score.
     */
    List<Risk> calculateOIRDRisk(Patient patient, Bundle medications, Bundle conditions, Bundle conditionsEnc);
}
