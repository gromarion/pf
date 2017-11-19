package com.itba.domain.repository.hibernate;

import com.itba.domain.model.User;
import com.itba.domain.repository.UserRepo;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class HibernateUserRepo extends AbstractHibernateRepo implements UserRepo {

	@Autowired
	public HibernateUserRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public User get(int userId) {
		return get(User.class, userId);
	}

	@Override
	public User getByUsername(String username) {
		List<User> result = find("from User where username = ?", username);
		return result.size() > 0 ? result.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		Query query = getSession().createQuery(
				"SELECT u FROM User u WHERE u.roles NOT IN ('ADMIN', 'GUEST') AND u.deleted_at is null");

		return query.list();
	}

	@Override
	public void deleteUser(int userId) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Query query = getSession().createQuery("UPDATE User u SET u.deleted_at = '" + ts + "' WHERE u.id = " + userId);
		query.executeUpdate();
	}

}