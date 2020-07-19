package com.mdt.prodigy.intersystems;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intersystems.enslib.pex.BusinessProcess;
import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.request.CDSServiceRequest;
import com.mdt.prodigy.service.IProdigyService;
import com.mdt.prodigy.service.ProdigyService;

public class ProdigyBusinessProcess extends BusinessProcess {

    IProdigyService prodigyService = null;
    ObjectMapper objectMapper = null;

    public void OnInit() throws Exception {
        prodigyService = new ProdigyService();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
    }

    public Object OnRequest(Object request) throws Exception {
        CDSServiceMessage message = (CDSServiceMessage) request;
        CDSServiceMessage output = new CDSServiceMessage();
        CDSServiceRequest serviceRequest = null;
        try{
            serviceRequest = objectMapper.readValue(message.getJson(), CDSServiceRequest.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Cards cards = prodigyService.getOIRDCards(serviceRequest);
        output.setJson(objectMapper.writeValueAsString(cards));

        return output;
    }

}
