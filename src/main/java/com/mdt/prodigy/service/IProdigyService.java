package com.mdt.prodigy.service;

import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.request.CDSServiceRequest;
import org.hl7.fhir.dstu3.model.Bundle;

public interface IProdigyService {


    Cards getORIDCards(CDSServiceRequest request);

    /**
     * Calculates the risk of OIRD (Opiod Induced Respiratory Depression) for a patient.
     * @param bundle
     * @return
     */
    int calculateOIRDRisk(Bundle bundle);
}
