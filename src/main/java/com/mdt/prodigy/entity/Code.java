package com.mdt.prodigy.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Code {

    private Type type;
    private String value;
}
