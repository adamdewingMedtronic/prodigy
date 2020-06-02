package com.mdt.taymyr.service;

import com.mdt.taymyr.dto.card.Cards;
import com.mdt.taymyr.dto.request.CDSServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Bundle;

@Slf4j
public class ProdigyService implements IProdigyService{

    @Override
    public Cards getORIDCards(CDSServiceRequest request) {
        // TODO  Debug code below
        Cards cards = new Cards();

        return null;
    }

    @Override
    public int calculateOIRDRisk(Bundle bundle) {
        log.debug("We are in calculateOIRDRisk");
        /*
        •	Age: unknown or <60 = 0; <70 = 8; <80 = 12; >=80 = 16
•	Sex: M = 8, F or unknown = 0
•	Opioid naïve: Y = 3 (meaning they’ve never been prescribed an opioid before)
•	Sleep disorder: Y = 5 (from the problem lists)
•	Chronic heart failure: Y = 7 (from the problem lists)
         */
        int risk = 0;
        risk += ageRisk(bundle);
        risk += sexRisk(bundle);
        risk += opiodNaiveRisk(bundle);
        risk += sleepDisorderRisk(bundle);
        risk += chronicHeartFailureRisk(bundle);
        return risk;
    }

    private int ageRisk(Bundle bundle) {
        int age = getAge(bundle);
        if (age >= 80) {
            return 16;
        } else if (age >= 70) {
            return 12;
        } else if (age >= 60) {
            return 8;
        }else{
            return 0;
        }
    }

    private int sexRisk(Bundle bundle){
        return "M".equalsIgnoreCase(getSex(bundle)) ? 8 : 0;
    }

    private int opiodNaiveRisk(Bundle bundle){
        return isOpiodNaive(bundle) ? 3 : 0;
    }

    private int sleepDisorderRisk(Bundle bundle){
        return isSleepDisorder(bundle) ? 5 : 0;
    }

    private int chronicHeartFailureRisk(Bundle bundle){
        return isChronicHeartFailure(bundle) ? 7 : 0;
    }

    private int getAge(Bundle bundle){
        //TODO
        return (int)Math.round(Math.random() * 91);
    }

    private String getSex(Bundle bundle){
        //TODO
        return Math.random() > .5 ? "M" : "F";
    }

    private boolean isOpiodNaive(Bundle bundle){
        //TODO
        return Math.random() > .5 ? true : false;
    }

    private boolean isSleepDisorderRisk(Bundle bundle){
        //TODO
        return Math.random() > .5 ? true : false;
    }

    private boolean isSleepDisorder(Bundle bundle){
        //TODO
        return Math.random() > .5 ? true : false;
    }

    private boolean isChronicHeartFailure(Bundle bundle){
        //TODO
        return Math.random() > .5 ? true : false;
    }
}
