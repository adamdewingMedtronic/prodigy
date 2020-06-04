package com.mdt.prodigy.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdt.prodigy.dto.card.Card;
import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.card.ProdigyResponseCard;
import com.mdt.prodigy.dto.request.CDSServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProdigyService implements IProdigyService{

    @Override
    public Cards getORIDCards(CDSServiceRequest request) {
        Patient patient = null;
        Bundle medications = null;
        Bundle conditions = null;
        Bundle conditionsEnc = null;
        Cards cards = new Cards();
        ProdigyResponseCard card = new ProdigyResponseCard();
        cards.getCards().add(card);
        FhirContext ctx = FhirContext.forDstu3();
        IParser parser = ctx.newJsonParser();
        log.debug(request.getPrefetch().getPatient().toString());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            patient = parser.parseResource(Patient.class, objectMapper.writeValueAsString(request.getPrefetch().getPatient()));
            medications = parser.parseResource(Bundle.class, objectMapper.writeValueAsString(request.getPrefetch().getMedications()));
            conditions = parser.parseResource(Bundle.class, objectMapper.writeValueAsString(request.getPrefetch().getConditions()));
            conditionsEnc = parser.parseResource(Bundle.class, objectMapper.writeValueAsString(request.getPrefetch().getConditionsEnc()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        card.setSummary(getSummary(calculateOIRDRisk(new Bundle())));
        card.setDetail("<div style=\\\"width: 700px; background: #FFFFF7;\\\">    <div style=\\\"height: 80px; font-size:24px; font-weight: bold; line-height: 40px; padding-left: 40px; color: black;\\\">        PRODIGY    </div>    <div style=\\\"height:220px; width: 80%; text-align: center; background: clear; margin: auto;\\\">        <div style=\\\"height:240px; width: 45%; background: clear; float: left;\\\">            <div                style=\\\"height:80px; width:80px; text-align: center; background: #E30504; border-radius: 80px; font-size: 36px; font-weight: bold; color: #fff; line-height: 80px; margin: auto;\\\">20            </div>            <h4 style=\\\"color: black; font-weight: lighter;\\\">PRODIGY SCORE</h4>            <h4 style=\\\"color: #E30504;\\\">HIGH RISK</h4>        </div>        <div style=\\\"height:240px; width: 55%; background: clear; float: left;\\\">            <p style=\\\"color: black; text-align: left;\\\">This patient is at HIGH RISK for opioid-induced respiratory depression. Consider placing the patient on capnography monitoring.</p>            <p style=\\\"color: #308DCD; text-align: left;\\\">Capnography Monitoring Policy</p>            <p style=\\\"text-align: left;\\\"><a style=\\\"color: #308DCD;\\\" href=\\\"https://www.medtronic.com/content/dam/covidien/library/us/en/product/capnography-monitoring/microstream-capnography-breath-monitoring-matters-info-sheet.pdf\\\" target=\\\"_blank\\\" rel=\\\"noopener noreferrer\\\">About Capnography</a></p>            <div style=\\\"height:55px; width: 100%; background: #EEEEEE;\\\">                <table style=\\\"width: 100%; height: 100%; font-size: 11px\\\">                    <tr>                        <td colspan=\\\"3\\\" style=\\\"font-weight: bold; color: grey; text-align: left;\\\">SCORE TIER LEGEND</td>                    </tr>                    <tr>                        <td style= \\\"text-align: left; color: grey; line-height: 12px;\\\">                            <div style=\\\"height:12px; width:12px; background: #77BC1F; border-radius: 12px; line-height: 12px; float: left;\\\"></div><span style=\\\"padding-left: 4px;\\\">0-7</span><span style=\\\"font-weight: bold; padding-left: 4px;\\\">Low</span>                        </td>                        <td style= \\\"text-align: left; color: grey; line-height: 12px;\\\">                            <div style=\\\"height:12px; width:12px; background: #F7A801; border-radius: 12px; line-height: 12px; float: left;\\\"></div><span style=\\\"padding-left: 4px;\\\">8-14</span><span style=\\\"font-weight: bold; padding-left: 4px;\\\">Intermediate</span>                        </td>                        <td style= \\\"text-align: left; color: grey; line-height: 12px;\\\">                            <div style=\\\"height:12px; width:12px; background: #E30504; border-radius: 12px; line-height: 12px; float: left;\\\"></div><span style=\\\"padding-left: 4px;\\\">15+</span><span style=\\\"font-weight: bold; padding-left: 4px;\\\">High</span>                        </td>                    </tr>                </table>            </div>        </div>    </div>    <div style=\\\"height:200px; width: 80%; background: clear; margin: auto;\\\">        <table style=\\\"width: 100%;\\\">            <tr>                <td style=\\\"font-weight: bold; color: grey; width: 45%;\\\">COMPONENT</td>                <td colspan=\\\"2\\\" style=\\\"color: grey;\\\"><span style=\\\"font-weight: bold;\\\"> SCORE</span> / VALUE</td>            </tr>            <tr>                <td rowspan=\\\"2\\\" style=\\\"font-weight: bold; color: black;\\\">Age</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">16</span>80 +</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">8</span>60 - 69</td>            </tr>            <tr>                <td style= \\\"color: grey; color: black; background: #FACE03;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">12</span>70 - 79</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>< 60</td> </tr> <tr>                <td style=\\\"font-weight: bold; color: black;\\\">Sex</td>                <td style= \\\"color: grey; color: black; background: #FACE03;\\\"><span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">8</span>Male</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>Female or Unk</td>            </tr>            <tr>                <td style=\\\"font-weight: bold; color: black;\\\">Opioid Naive</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">3</span>Yes</td>                <td style= \\\"color: grey; color: black; background: #FACE03;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>No</td>            </tr>            <tr>                <td style=\\\"font-weight: bold; color: black;\\\">Sleep Disorder</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">5</span>Yes</td>                <td style= \\\"color: grey; color: black; background: #FACE03;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>No</td>            </tr>            <tr>                <td style=\\\"font-weight: bold; color: black;\\\">Chronic Heart Failure</td>                <td style= \\\"color: grey;\\\"><span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">7</span>Yes</td>                <td style= \\\"color: grey; color: black; background: #FACE03;\\\"><span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>No</td>            </tr>        </table>    </div></div>");
        return cards;
    }

    @Override
    public int calculateOIRDRisk(Bundle bundle) {
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

    private String getSummary(int risk){
        // TODO
        String riskText = null;
        if(risk >= 15){
            riskText = "High Risk";
        }else if(risk >= 8 && risk <= 14){
            riskText = "Intermediate Risk";
        }else if(risk < 8){
            riskText = "Low Risk";
        }
        return riskText;
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
