package com.mdt.prodigy.dao;

import com.mdt.prodigy.entity.Code;

public class CodeDao extends Dao<Code> {
    @Override
    protected Class<Code> getClassType() {
        return Code.class;
    }
}
