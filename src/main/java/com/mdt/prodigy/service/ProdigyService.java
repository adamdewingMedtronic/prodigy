package com.mdt.prodigy.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import com.mdt.prodigy.enums.RiskText;
import com.mdt.prodigy.enums.RiskType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdigyService implements IProdigyService {

    FhirContext ctx = null;
    IParser parser = null;

    public ProdigyService(){
        ctx = FhirContext.forDstu3();
        parser = ctx.newJsonParser();
    }

    @Override
    public Cards getOIRDCards(CDSServiceRequest request) {
        Patient patient = null;
        Bundle medications = null;
        Bundle conditions = null;
        Bundle conditionsEnc = null;
        Cards cards = new Cards();
        ProdigyResponseCard card = new ProdigyResponseCard();
        cards.getCards().add(card);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
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
        /***************************************
         Calculate OIRD Risks
         */
        Risk ageRisk = null;
        Risk sexRisk = null;
        Risk chronicHeartFailureRisk = null;
        Risk opiodNaiveRisk = null;
        Risk sleepDisorderRisk = null;
        List<Risk> risks = new ArrayList<>();
        ageRisk = new Risk(ageRiskScore(patient), RiskType.AGE);
        risks.add(ageRisk);
        sexRisk = new Risk(sexRisk(patient), RiskType.SEX);
        risks.add(sexRisk);
        chronicHeartFailureRisk = new Risk(chronicHeartFailureRisk(conditions), RiskType.CHRONIC_HEART_FAILURE);
        risks.add(chronicHeartFailureRisk);
        opiodNaiveRisk = new Risk(opiodNaiveRisk(medications), RiskType.OPIOD_NAIVE);
        risks.add(opiodNaiveRisk);
        sleepDisorderRisk = new Risk(sleepDisorderRisk(conditions), RiskType.SLEEP_DISORDER);
        risks.add(sleepDisorderRisk);

        /***************************************
         Build HTML
         */
        int riskScore = 0;
        for (Risk risk : risks) {
            riskScore += risk.getScore();
        }
        RiskText riskText = getSummary(riskScore);
        String html = buildHTML(risks, riskScore, riskText, patient);
        html = setAgeRiskCss(patient, html);
        html = setChronicHeartFailureRiskCss(chronicHeartFailureRisk.getScore(), html);
        html = setOpiodNaiveRiskCss(opiodNaiveRisk.getScore(), html);
        html = setSexRiskCss(sexRisk.getScore(), html);
        html = setSleepDisorderRiskCss(sleepDisorderRisk.getScore(), html);
        /***************************************/

        card.setSummary(riskText.getText());
        card.setDetail(html);
        return cards;
    }

    private int ageRiskCategory(Patient patient) {
        int age = getAge(patient);
        if (age >= 80) {
            return 4;
        } else if (age >= 70) {
            return 3;
        } else if (age >= 60) {
            return 2;
        } else {
            return 1;
        }
    }

    private int ageRiskScore(Patient patient) {
        int category = ageRiskCategory(patient);
        if (category == 4) {
            return 16;
        } else if (category == 3) {
            return 12;
        } else if (category == 2) {
            return 8;
        } else {
            return 0;
        }
    }

    private String buildHTML(List<Risk> risks, int riskScore, RiskText riskText, Patient patient) {
        String html = getBaseHtml();
        html = html.replaceAll("RISK_TEXT", riskText.getText());
        html = html.replaceAll("RISK_COLOR", riskText.getColor());
        html = html.replaceAll("RISK_SCORE", String.valueOf(riskScore));
        if (riskScore >= 8) {
            html = html.replaceAll("CAPNOGRAPHY_MONITORING", "Consider placing the patient on capnography monitoring.");
        } else {
            html = html.replaceAll("CAPNOGRAPHY_MONITORING", "");
        }
        html = setAgeRiskCss(patient, html);
        return html;
    }

    private int chronicHeartFailureRisk(Bundle bundle) {
        return isChronicHeartFailure(bundle) ? 7 : 0;
    }

    private int getAge(Patient patient) {
        PatientHelper patientHelper = new PatientHelper(patient);
        return patientHelper.getAge();
    }

    private String getBaseHtml() {
        return "<style>" +
                "    .red {" +
                "        background: red;" +
                "    }" +
                "    .yellow{" +
                "        background: #FACE03;" +
                "    }" +
                "    .green {" +
                "        background: green;" +
                "    }" +
                "    .risk{" +
                "        color: black;" +
                "        background: #FACE03;" +
                "    }" +
                "</style>" +
                "<div style=\"width: 700px; background: #FFFFF7;\">" +
                "    <div style=\"height: 80px; font-size:24px; font-weight: bold; line-height: 40px; padding-left: 40px; color: black;\"> PRODIGY</div>" +
                "    <div style=\"height:220px; width: 80%; text-align: center; background: clear; margin: auto;\">" +
                "        <div style=\"height:240px; width: 45%; background: clear; float: left;\">" +
                "            <div class=\"RISK_COLOR\" style=\"height:80px; width:80px; text-align: center; border-radius: 80px; font-size: 36px; font-weight: bold; color: #fff; line-height: 80px; margin: auto;\">RISK_SCORE</div>" +
                "            <h4 style=\"color: black; font-weight: lighter;\">PRODIGY SCORE</h4>" +
                "        </div>" +
                "        <div style=\"height:240px; width: 55%; background: clear; float: left;\">" +
                "            <p style=\"color: black; text-align: left;\">This patient is at <b>RISK_TEXT</b> for opioid-induced respiratory depression. CAPNOGRAPHY_MONITORING</p>" +
                "            <p style=\"color: #308DCD; text-align: left;\">Capnography Monitoring Policy</p>" +
                "            <p style=\"text-align: left;\">" +
                "                <a style=\"color: #308DCD;\" href=\"https://www.medtronic.com/content/dam/covidien/library/us/en/product/capnography-monitoring/microstream-capnography-breath-monitoring-matters-info-sheet.pdf\" target=\"_blank\" rel=\"noopener noreferrer\">About Capnography</a>" +
                "            </p>" +
                "            <div style=\"height:55px; width: 100%; background: #EEEEEE;\">" +
                "                <table style=\"width: 100%; height: 100%; font-size: 11px\">" +
                "                    <tr>" +
                "                        <td colspan=\"3\" style=\"font-weight: bold; color: grey; text-align: left;\">SCORE TIER LEGEND</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style=\"text-align: left; color: grey; line-height: 12px;\">" +
                "                            <div style=\"height:12px; width:12px; background: #77BC1F; border-radius: 12px; line-height: 12px; float: left;\"></div>" +
                "                            <span style=\"padding-left: 4px;\">0-7</span>" +
                "                            <span style=\"font-weight: bold; padding-left: 4px;\">Low</span>" +
                "                        </td>" +
                "                        <td style=\"text-align: left; color: grey; line-height: 12px;\">" +
                "                            <div style=\"height:12px; width:12px; background: #F7A801; border-radius: 12px; line-height: 12px; float: left;\"></div>" +
                "                            <span style=\"padding-left: 4px;\">8-14</span>" +
                "                            <span style=\"font-weight: bold; padding-left: 4px;\">Intermediate</span>" +
                "                        </td>" +
                "                        <td style=\"text-align: left; color: grey; line-height: 12px;\">" +
                "                            <div style=\"height:12px; width:12px; background: #E30504; border-radius: 12px; line-height: 12px; float: left;\"></div>" +
                "                            <span style=\"padding-left: 4px;\">15+</span>" +
                "                            <span style=\"font-weight: bold; padding-left: 4px;\">High</span>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "    <div style=\"height:200px; width: 80%; background: clear; margin: auto;\">" +
                "        <table style=\"width: 100%; color: gray\">" +
                "            <tr>" +
                "                <td style=\"font-weight: bold; color: grey; width: 45%;\">COMPONENT</td>" +
                "                <td colspan=\"2\" style=\"color: grey;\">" +
                "                    <span style=\"font-weight: bold;\"> SCORE</span> / VALUE" +
                "                </td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td rowspan=\"2\" style=\"font-weight: bold; color: black;\">Age</td>" +
                "                <td class=\"AGE_RISK4\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">16</span>80 +" +
                "                </td>" +
                "                <td class=\"AGE_RISK2\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">8</span>60 - 69" +
                "                </td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td class=\"AGE_RISK3\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">12</span>70 - 79" +
                "                </td>" +
                "                <td class=\"AGE_RISK1\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>< 60" +
                "                </td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td style=\"font-weight: bold; color: black;\">Sex</td>" +
                "                <td class=\"SEX_RISK_MALE\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">8</span>Male" +
                "                </td>" +
                "                <td class=\"SEX_RISK_FEMALE\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>Female or Unk" +
                "                </td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td style=\"font-weight: bold; color: black;\">Opioid Naive</td>" +
                "                <td class=\"OPIOD_NAIVE_RISK_YES\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">3</span>Yes" +
                "                </td>" +
                "                <td class=\"OPIOD_NAIVE_RISK_NO\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>No" +
                "                </td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td style=\"font-weight: bold; color: black;\">Sleep Disorder</td>" +
                "                <td class=\"SLEEP_DISORDER_YES\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">5</span>Yes" +
                "                </td>" +
                "                <td class=\"SLEEP_DISORDER_NO\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>No" +
                "                </td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td style=\"font-weight: bold; color: black;\">Chronic Heart Failure</td>" +
                "                <td class=\"CHRONIC_HEART_FAILURE_RISK_YES\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">7</span>Yes" +
                "                </td>" +
                "                <td class=\"CHRONIC_HEART_FAILURE_RISK_NO\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>No" +
                "                </td>" +
                "            </tr>" +
                "        </table>" +
                "    </div>" +
                "</div>";
    }

    private String getSex(Patient patient) {
        PatientHelper patientHelper = new PatientHelper(patient);
        return patientHelper.getGender();
    }

    private RiskText getSummary(int risk) {
        RiskText riskText = null;
        if (risk >= 15) {
            riskText = RiskText.HIGH;
        } else if (risk >= 8 && risk <= 14) {
            riskText = RiskText.INTERMEDIATE;
        } else if (risk < 8) {
            riskText = RiskText.LOW;
        }
        return riskText;
    }

    private boolean isChronicHeartFailure(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        String data = parser.encodeResourceToString(bundle);
        for (String value : HeartFailureCodes.values) {
            if (data.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the bundle contains MedicationStatements that contain opiods.
     *
     * @param bundle
     * @return True if the bundle does not contain any MedicationStatements that are opiods.
     */
    private boolean isOpiodNaive(Bundle bundle) {
        if (bundle == null) {
            return true;
        }
        String data = null;
        data = parser.encodeResourceToString(bundle);
        for (String value : OpiodCodes.values) {
            if (data.contains(value)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSleepDisorder(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        String data = parser.encodeResourceToString(bundle);
        for (String value : SleepDisorderCodes.values) {
            if (data.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private int opiodNaiveRisk(Bundle bundle) {
        return isOpiodNaive(bundle) ? 3 : 0;
    }

    private String setAgeRiskCss(Patient patient, String html) {
        int category = ageRiskCategory(patient);
        for (int i = 1; i <= 4; i++) {
            if (i == category) {
                html = html.replaceAll("AGE_RISK".concat(Integer.toString(i)), "risk");
            } else {
                html = html.replaceAll("AGE_RISK".concat(Integer.toString(i)), "");
            }
        }
        return html;
    }

    private String setChronicHeartFailureRiskCss(int chronicHeartFailureRisk, String html) {
        if (chronicHeartFailureRisk > 0) {
            html = html.replaceAll("CHRONIC_HEART_FAILURE_RISK_YES", "risk");
            html = html.replaceAll("CHRONIC_HEART_FAILURE_RISK_NO", "");
        } else {
            html = html.replaceAll("CHRONIC_HEART_FAILURE_RISK_YES", "");
            html = html.replaceAll("CHRONIC_HEART_FAILURE_RISK_NO", "risk");

        }
        return html;
    }

    private String setOpiodNaiveRiskCss(int opiodNaiveRisk, String html) {
        if (opiodNaiveRisk > 0) {
            html = html.replaceAll("OPIOD_NAIVE_RISK_YES", "risk");
            html = html.replaceAll("OPIOD_NAIVE_RISK_NO", "");
        } else {
            html = html.replaceAll("OPIOD_NAIVE_RISK_YES", "");
            html = html.replaceAll("OPIOD_NAIVE_RISK_NO", "risk");

        }
        return html;
    }

    private String setSexRiskCss(int sexRisk, String html) {
        if (sexRisk > 0) {
            html = html.replaceAll("SEX_RISK_MALE", "risk");
            html = html.replaceAll("SEX_RISK_FEMALE", "");
        } else {
            html = html.replaceAll("SEX_RISK_MALE", "");
            html = html.replaceAll("SEX_RISK_FEMALE", "risk");

        }
        return html;
    }

    private String setSleepDisorderRiskCss(int sleepDisorderRisk, String html) {
        if (sleepDisorderRisk > 0) {
            html = html.replaceAll("SLEEP_DISORDER_YES", "risk");
            html = html.replaceAll("SLEEP_DISORDER_NO", "");
        } else {
            html = html.replaceAll("SLEEP_DISORDER_YES", "");
            html = html.replaceAll("SLEEP_DISORDER_NO", "risk");

        }
        return html;
    }

    private int sexRisk(Patient patient) {
        return "male".equalsIgnoreCase(getSex(patient)) ? 8 : 0;
    }

    private int sleepDisorderRisk(Bundle bundle) {
        return isSleepDisorder(bundle) ? 5 : 0;
    }
}
