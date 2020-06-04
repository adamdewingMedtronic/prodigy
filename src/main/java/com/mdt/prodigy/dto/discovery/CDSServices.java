package com.mdt.prodigy.dto.discovery;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CDSServices {

    private List<CDSService> services = new ArrayList<>();
}
