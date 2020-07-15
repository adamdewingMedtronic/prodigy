package com.mdt.prodigy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "code")
@Data
public class Code implements Serializable {

    @EmbeddedId
    private CodePK codePK;

    public Code(){

    }
    public Code(CodePK codePK){
        this.codePK = codePK;
    }

    public Code(String type, String value){
        this.codePK = new CodePK(type, value);
    }

    public String getType(){
        return this.getCodePK().getType();
    }

    public String getValue(){
        return this.getCodePK().getValue();
    }

    public CodePK getCodePK(){
        return this.codePK;
    }
    public Code setCodePK(CodePK codePK){
        this.codePK = codePK;
        return this;
    }
}
