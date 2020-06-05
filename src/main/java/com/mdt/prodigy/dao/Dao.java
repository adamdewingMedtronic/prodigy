package com.mdt.prodigy.dao;

import com.mdt.prodigy.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Dao<T> {

    private SessionFactory sessionFactory;

    /**
     *
     * @param o
     */
    public Serializable save(final T o){
        return getSession().save(o);
    }

    /**
     *
     * @param o
     */
    public void saveOrUpdate(final T o){
        getSession().save(o);
    }

    /**
     *
     * @param o
     */
    public void delete(final T o){
        getSession().delete(o);
    }

    public T find(final Object obj){
        return getSession().find(getClassType(), obj);
    }


    /**
     *
     * @return
     */
    public List<T> findAll() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(getClassType());
        criteria.from(getClassType());
        List<T> data = getSession().createQuery(criteria).getResultList();
        return data == null ? new ArrayList<>() : data;
    }

    protected Session getSession(){
        return getSessionFactory().getCurrentSession();
    }

    protected SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            sessionFactory = HibernateUtil.getSessionFactory();
        }
        return sessionFactory;
    }

    protected abstract Class<T> getClassType();
}
