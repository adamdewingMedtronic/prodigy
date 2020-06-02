package com.mdt.taymyr.service;

import com.mdt.taymyr.dto.card.Cards;
import com.mdt.taymyr.dto.request.CDSServiceRequest;
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
