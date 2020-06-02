package com.mdt.taymyr.dto.card;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cards {

    List<Card> cards = new ArrayList<>();
}
