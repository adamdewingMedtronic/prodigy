package com.mdt.prodigy.dto;

import com.mdt.prodigy.enums.RiskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Risk {

    private int score = 0;
    private RiskType riskType;
}
