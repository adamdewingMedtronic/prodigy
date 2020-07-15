package com.mdt.prodigy.dao;

import com.mdt.prodigy.entity.Code;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

public class CodeDao extends Dao<Code> {
    @Override
    protected Class<Code> getClassType() {
        return Code.class;
    }

    public @NotNull List<Code> findByType(String type){
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Code> criteria = cb.createQuery(getClassType());
        Root<Code> root = criteria.from(getClassType());
        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get("type"), type);
        criteria.select(root).where(predicates);
        List<Code> data = getSession().createQuery(criteria).getResultList();
        return data == null ? Collections.emptyList() : data;
    }

    public @NotNull List<Code> findByTypeCodeContains(String type, String codeContains){
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Code> criteria = cb.createQuery(getClassType());
        Root<Code> root = criteria.from(getClassType());
        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get("type"), type);
        predicates[1] = cb.like(root.get("value"), codeContains);
        criteria.select(root).where(predicates);
        List<Code> data = getSession().createQuery(criteria).getResultList();
        return data == null ? Collections.emptyList() : data;
    }
}


