package com.mdt.prodigy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdt.prodigy.dto.card.Card;
import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.request.CDSServiceRequest;
import com.mdt.prodigy.util.FileUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProdigyServiceTest {

    private IProdigyService prodigyService = null;
    private ObjectMapper objectMapper = null;

    private Bundle bundle = null;

    @BeforeEach
    public void setUp() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
        this.prodigyService = new ProdigyService();
    }

    @AfterEach
    void tearDown() {


    }

    @Test
    void getORIDCards(){
        String input = FileUtil.load("sample_input.json");
        System.out.println(input);
        CDSServiceRequest request = null;
        try{
            request = objectMapper.readValue(input, CDSServiceRequest.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Cards cards = prodigyService.getOIRDCards(request);

        for(Card card: cards.getCards()){
            FileUtil.save("c:/Temp/test.html", card.getDetail());
            try {
                System.out.println(objectMapper.writeValueAsString(card));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void calculateOIRDRisk() {
    }
}