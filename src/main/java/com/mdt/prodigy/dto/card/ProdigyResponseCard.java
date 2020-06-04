package com.mdt.prodigy.dto.card;

import lombok.Data;

@Data
public class ProdigyResponseCard extends Card {

    private Extension extension = new Extension();

    public ProdigyResponseCard(){
        setIndicator("warning");
        this.getExtension().getExtension().put("com.epic.cdshooks.card.detail.content-type", "text/html");
    }
}
