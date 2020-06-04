package com.mdt.prodigy.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Type {
    private String type;
    private String description;
}
