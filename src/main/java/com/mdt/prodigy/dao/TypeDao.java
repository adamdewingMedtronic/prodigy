package com.mdt.prodigy.dao;

import com.mdt.prodigy.entity.Type;

public class TypeDao extends Dao<Type> {

    @Override
    protected Class<Type> getClassType(){
        return Type.class;
    }
}
