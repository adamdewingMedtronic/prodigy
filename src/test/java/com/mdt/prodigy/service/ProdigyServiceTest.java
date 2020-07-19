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
import org.junit.jupiter.api.Assertions;
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
    public void getORIDCards_age_30(){
        Cards cards = getORIDCards("sample_input_30.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>< 60" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
        FileUtil.save("c:/Temp/test_age_30.html", html);
    }

    @Test
    public void getORIDCards_age_60(){
        Cards cards = getORIDCards("sample_input_60.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">8</span>60 - 69" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
    }
    @Test
    public void getORIDCards_age_70(){
        Cards cards = getORIDCards("sample_input_70.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">12</span>70 - 79" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
    }
    @Test
    public void getORIDCards_age_80(){
        Cards cards = getORIDCards("sample_input_80.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">16</span>80 +" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
    }
    @Test
    public void getORIDCards_chronic_heart_failure(){
        Cards cards = getORIDCards("sample_input_chronic_heart_failure.json");
        String html = cards.getCards().get(0).getDetail();
        String expected1 = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">7</span>Yes" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected1), "The following string did not contain: " + expected1 + " " + html);
        String expected2 = "                <td class=\"\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>No" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected1), "The following string did not contain: " + expected2 + " " + html);
    }
    @Test
    public void getORIDCards_input_female(){
        Cards cards = getORIDCards("sample_input_female.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td class=\"\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">8</span>Male" +
                "                </td>" +
                "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>Female or Unk" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
    }
    @Test
    public void getORIDCards_input_male_opiod_naive_30(){
        Cards cards = getORIDCards("sample_input_male_opiod_naive_30.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">3</span>Yes" +
                "                </td>" +
                "                <td class=\"\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>No" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
        expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>< 60" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
        expected = "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">8</span>Male" +
                "                </td>" +
                "                <td class=\"\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>Female or Unk" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);
    }
    @Test
    public void getORIDCards_sleep_disorder(){
        Cards cards = getORIDCards("sample_input_sleep_disorder.json");
        String html = cards.getCards().get(0).getDetail();
        String expected = "                <td style=\"font-weight: bold; color: black;\">Sleep Disorder</td>" +
                "                <td class=\"risk\">" +
                "                    <span style=\"font-weight: bold; padding-right: 26px; padding-left: 4px;\">5</span>Yes" +
                "                </td>" +
                "                <td class=\"\">" +
                "                    <span style=\"font-weight: bold; padding-right: 20px; padding-left: 4px;\">0</span>No" +
                "                </td>";
        Assertions.assertEquals(true, html.contains(expected), "The following string did not contain: " + expected + " " + html);

    }

    private Cards getORIDCards(String fileName){
        String input = FileUtil.load(fileName);
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
            FileUtil.save("c:/Temp/"+ fileName.replaceAll("\\.json", ".html"), card.getDetail());
//            try {
//                System.out.println(objectMapper.writeValueAsString(card));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
        }
        return cards;
    }

    @Test
    void calculateOIRDRisk() {
    }
}