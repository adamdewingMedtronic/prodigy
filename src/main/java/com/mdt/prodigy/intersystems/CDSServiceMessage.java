package com.mdt.prodigy.intersystems;

import com.intersystems.enslib.pex.Message;
import lombok.Data;
import lombok.Getter;

@Data
public class CDSServiceMessage extends Message {

    private String json;
}

// com.mdt.prodigy.intersystems.CDSServiceMessage