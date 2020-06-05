package com.mdt.prodigy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@AllArgsConstructor
@IdClass(CodePK.class)
public class Code {

    @Id
    String type;

    @Id
    String value;
}
