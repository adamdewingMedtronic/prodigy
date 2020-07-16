package com.mdt.prodigy.service;

import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.request.CDSServiceRequest;

public interface IProdigyService {

    /**
     * Gets cards for OIRD (Opiod Induced Respiratory Depression) Risk.
     * @param request
     * @return
     */
    Cards getOIRDCards(CDSServiceRequest request);
}
