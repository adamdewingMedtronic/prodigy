package com.mdt.prodigy.dto.card;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Extension {
    Map<String, String> extension = new HashMap<>();
}
