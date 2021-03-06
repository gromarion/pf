package com.itba.domain.repository.hibernate;

import java.io.Serializable;
import java.util.List;

public interface HibernateRepo {
    public <T> T get(Class<T> type, Serializable id);
    public <T> List<T> find(String hql, Object... params);
    public Serializable save(Object o);
    public void delete(Object o);
    public <T> Long count(Class<T> type);
}
