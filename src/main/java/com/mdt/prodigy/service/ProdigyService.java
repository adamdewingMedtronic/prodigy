package com.mdt.prodigy.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdt.fhir.resource.PatientHelper;
import com.mdt.prodigy.dto.HeartFailureCodes;
import com.mdt.prodigy.dto.OpiodCodes;
import com.mdt.prodigy.dto.Risk;
import com.mdt.prodigy.dto.SleepDisorderCodes;
import com.mdt.prodigy.dto.card.Cards;
import com.mdt.prodigy.dto.card.ProdigyResponseCard;
import com.mdt.prodigy.dto.request.CDSServiceRequest;
import com.mdt.prodigy.enums.RiskType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class ProdigyService implements IProdigyService {


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
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(request.getPrefetch().getPatient());
            if (request.getPrefetch() != null) {
                if (request.getPrefetch().getPatient() != null) {
                    patient = parser.parseResource(Patient.class, objectMapper.writeValueAsString(request.getPrefetch().getPatient()));
                }
                if (request.getPrefetch().getMedications() != null) {
                    medications = parser.parseResource(Bundle.class, objectMapper.writeValueAsString(request.getPrefetch().getMedications()));
                }
                if (request.getPrefetch().getConditions() != null) {
                    conditions = parser.parseResource(Bundle.class, objectMapper.writeValueAsString(request.getPrefetch().getConditions()));
                }
                if (request.getPrefetch().getConditionsEnc() != null) {
                    conditionsEnc = parser.parseResource(Bundle.class, objectMapper.writeValueAsString(request.getPrefetch().getConditionsEnc()));
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<Risk> risks = calculateOIRDRisk(patient, medications, conditions, conditionsEnc);

        int riskScore = 0;
        for(Risk risk : risks){
            riskScore += risk.getScore();
        }

        card.setSummary(getSummary(riskScore));
        card.setDetail(buildHTML(risks, riskScore, card.getSummary()));
        return cards;
    }

    private String buildHTML(List<Risk> risks, int riskScore, String riskText){
        String html = getBaseHtml();
        html = html.replaceAll("RISK_TEXT", riskText);
        html.replaceAll("RISK_SCORE", String.valueOf(riskScore));
        if(riskScore >= 8){
            html = html.replaceAll("CAPNOGRAPHY_MONITORING", "Consider placing the patient on capnography monitoring.");
        }else{
            html = html.replaceAll("CAPNOGRAPHY_MONITORING", "");
        }
        System.out.println(html);
        return html;
    }

    private String getSummary(int risk) {
        // TODO
        System.out.println("risk:" + risk);
        String riskText = null;
        if (risk >= 15) {
            riskText = "HIGH RISK";
        } else if (risk >= 8 && risk <= 14) {
            riskText = "INTERDIATE RISK";
        } else if (risk < 8) {
            riskText = "LOW RISK";
        }
        return riskText;
    }

    @Override
    public List<Risk> calculateOIRDRisk(Patient patient, Bundle medications, Bundle conditions, Bundle conditionsEnc) {
        List<Risk> risks = new ArrayList<>();
        risks.add(new Risk(ageRisk(patient), RiskType.AGE));
        risks.add(new Risk(sexRisk(patient), RiskType.SEX));
        risks.add(new Risk(chronicHeartFailureRisk(conditions), RiskType.CHRONIC_HEART_FAILURE));
        risks.add(new Risk(opiodNaiveRisk(medications), RiskType.OPIOD_NAIVE));
        risks.add(new Risk(chronicHeartFailureRisk(conditions), RiskType.SLEEP_DISORDER));
        return risks;
    }

    private int ageRisk(Patient patient) {
        int age = getAge(patient);
        if (age >= 80) {
            return 16;
        } else if (age >= 70) {
            return 12;
        } else if (age >= 60) {
            return 8;
        }else {
            return 0;
        }
    }

    private int sexRisk(Patient patient) {
        return "male".equalsIgnoreCase(getSex(patient)) ? 8 : 0;
    }

    private int opiodNaiveRisk(Bundle bundle) {
        return isOpiodNaive(bundle) ? 3 : 0;
    }

    private int sleepDisorderRisk(Bundle bundle) {
        return isSleepDisorder(bundle) ? 5 : 0;
    }

    private int chronicHeartFailureRisk(Bundle bundle) {
        return isChronicHeartFailure(bundle) ? 7 : 0;
    }

    private int getAge(Patient patient) {
        PatientHelper patientHelper = new PatientHelper(patient);
        return patientHelper.getAge();
    }

    private String getSex(Patient patient) {
        PatientHelper patientHelper = new PatientHelper(patient);
        return patientHelper.getGender();
    }

    /**
     * Determines if the bundle contains MedicationStatements that contain opiods.
     *
     * @param bundle
     * @return True if the bundle does not contain any MedicationStatements that are opiods.
     */
    private boolean isOpiodNaive(Bundle bundle) {
        if(bundle == null){
            return true;
        }
        String data = bundle.toString();
        for (String value : OpiodCodes.values) {
            if (data.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSleepDisorder(Bundle bundle) {
        if(bundle == null){
            return false;
        }
        String data = bundle.toString();
        for (String value : SleepDisorderCodes.values) {
            if (data.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChronicHeartFailure(Bundle bundle) {
        if(bundle == null){
            return false;
        }
        String data = bundle.toString();
        for (String value : HeartFailureCodes.values) {
            if (data.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private String getBaseHtml(){
        return "<div style=\\\"width: 700px; background: #FFFFF7;\\\">\n" +
                "\t<div style=\\\"height: 80px; font-size:24px; font-weight: bold; line-height: 40px; padding-left: 40px; color: black;\\\">        PRODIGY    </div>\n" +
                "\t<div style=\\\"height:220px; width: 80%; text-align: center; background: clear; margin: auto;\\\">\n" +
                "\t\t<div style=\\\"height:240px; width: 45%; background: clear; float: left;\\\">\n" +
                "\t\t\t<div                style=\\\"height:80px; width:80px; text-align: center; background: #E30504; border-radius: 80px; font-size: 36px; font-weight: bold; color: #fff; line-height: 80px; margin: auto;\\\">20            </div>\n" +
                "\t\t\t<h4 style=\\\"color: black; font-weight: lighter;\\\">PRODIGY SCORE</h4>\n" +
                "\t\t\t<h4 style=\\\"color: #E30504;\\\">RISK_SCORE</h4>\n" +
                "\t\t</div>\n" +
                "\t\t<div style=\\\"height:240px; width: 55%; background: clear; float: left;\\\">\n" +
                "\t\t\t<p style=\\\"color: black; text-align: left;\\\">This patient is at RISK_TEXT for opioid-induced respiratory depression. CAPNOGRAPHY_MONITORING</p>\n" +
                "\t\t\t<p style=\\\"color: #308DCD; text-align: left;\\\">Capnography Monitoring Policy</p>\n" +
                "\t\t\t<p style=\\\"text-align: left;\\\">\n" +
                "\t\t\t\t<a style=\\\"color: #308DCD;\\\" href=\\\"https://www.medtronic.com/content/dam/covidien/library/us/en/product/capnography-monitoring/microstream-capnography-breath-monitoring-matters-info-sheet.pdf\\\" target=\\\"_blank\\\" rel=\\\"noopener noreferrer\\\">About Capnography</a>\n" +
                "\t\t\t</p>\n" +
                "\t\t\t<div style=\\\"height:55px; width: 100%; background: #EEEEEE;\\\">\n" +
                "\t\t\t\t<table style=\\\"width: 100%; height: 100%; font-size: 11px\\\">\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td colspan=\\\"3\\\" style=\\\"font-weight: bold; color: grey; text-align: left;\\\">SCORE TIER LEGEND</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td style= \\\"text-align: left; color: grey; line-height: 12px;\\\">\n" +
                "\t\t\t\t\t\t\t<div style=\\\"height:12px; width:12px; background: #77BC1F; border-radius: 12px; line-height: 12px; float: left;\\\"></div>\n" +
                "\t\t\t\t\t\t\t<span style=\\\"padding-left: 4px;\\\">0-7</span>\n" +
                "\t\t\t\t\t\t\t<span style=\\\"font-weight: bold; padding-left: 4px;\\\">Low</span>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t<td style= \\\"text-align: left; color: grey; line-height: 12px;\\\">\n" +
                "\t\t\t\t\t\t\t<div style=\\\"height:12px; width:12px; background: #F7A801; border-radius: 12px; line-height: 12px; float: left;\\\"></div>\n" +
                "\t\t\t\t\t\t\t<span style=\\\"padding-left: 4px;\\\">8-14</span>\n" +
                "\t\t\t\t\t\t\t<span style=\\\"font-weight: bold; padding-left: 4px;\\\">Intermediate</span>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t<td style= \\\"text-align: left; color: grey; line-height: 12px;\\\">\n" +
                "\t\t\t\t\t\t\t<div style=\\\"height:12px; width:12px; background: #E30504; border-radius: 12px; line-height: 12px; float: left;\\\"></div>\n" +
                "\t\t\t\t\t\t\t<span style=\\\"padding-left: 4px;\\\">15+</span>\n" +
                "\t\t\t\t\t\t\t<span style=\\\"font-weight: bold; padding-left: 4px;\\\">High</span>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "\t<div style=\\\"height:200px; width: 80%; background: clear; margin: auto;\\\">\n" +
                "\t\t<table style=\\\"width: 100%;\\\">\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td style=\\\"font-weight: bold; color: grey; width: 45%;\\\">COMPONENT</td>\n" +
                "\t\t\t\t<td colspan=\\\"2\\\" style=\\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold;\\\"> SCORE</span> / VALUE\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td rowspan=\\\"2\\\" style=\\\"font-weight: bold; color: black;\\\">Age</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">16</span>80 +\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">8</span>60 - 69\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td style= \\\"color: grey; color: black; background: #FACE03;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">12</span>70 - 79\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>< 60\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td style=\\\"font-weight: bold; color: black;\\\">Sex</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey; color: black; background: #FACE03;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">8</span>Male\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>Female or Unk\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td style=\\\"font-weight: bold; color: black;\\\">Opioid Naive</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">3</span>Yes\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey; color: black; background: #FACE03;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>No\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td style=\\\"font-weight: bold; color: black;\\\">Sleep Disorder</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">5</span>Yes\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey; color: black; background: #FACE03;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>No\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td style=\\\"font-weight: bold; color: black;\\\">Chronic Heart Failure</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 26px; padding-left: 4px;\\\">7</span>Yes\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<td style= \\\"color: grey; color: black; background: #FACE03;\\\">\n" +
                "\t\t\t\t\t<span style=\\\"font-weight: bold; padding-right: 20px; padding-left: 4px;\\\">0</span>No\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t</table>\n" +
                "\t</div>\n" +
                "</div>";
    }
}
