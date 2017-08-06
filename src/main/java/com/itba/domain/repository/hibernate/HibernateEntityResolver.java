package com.itba.domain.repository.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.itba.domain.EntityResolver;
import com.itba.domain.PersistentEntity;

@Component
@SuppressWarnings("unchecked")
public class HibernateEntityResolver implements EntityResolver {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateEntityResolver(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <T> T fetch(Class<T> type, Integer id) {
        try {
            return (T) getSession().get(type, id);
        } catch (HibernateException ex) {
            throw new HibernateException("Problem while fetching (" + type.getSimpleName() + ", " + id.toString() + ")", ex);
        }
    }

    @Override
    public Integer getId(final Object object) {
        Assert.isInstanceOf(PersistentEntity.class, object, "This entity resolver only hanldes objects implementing PersistentEntity");
        try {
            getSession().flush();
            PersistentEntity entity = (PersistentEntity) object;
            if (entity == null) {
                throw new TransientObjectException("Object doesn't have an id associated!");
            }
            return entity.getId();
        } catch (HibernateException ex) {
            throw new HibernateException("Problem while retrieving id for " + object.toString(), ex);
        }
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
