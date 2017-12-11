package com.itba.domain.repository.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluationSession;
import com.itba.domain.repository.EvaluatedResourceRepo;

@Repository
public class HibernateEvaluatedResourceRepo extends AbstractHibernateRepo implements EvaluatedResourceRepo {

	private static final int LIMIT = 10;

	@Autowired
	public HibernateEvaluatedResourceRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public EvaluatedResource get(int evaluatedResourceId) {
		return get(EvaluatedResource.class, evaluatedResourceId);
	}

	private String adapt(String s) {
		return s.replaceAll("'", "''");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Optional<EvaluatedResource> getResourceForSession(final EvaluationSession session, final String resource) {
		Query query = getSession().createQuery("SELECT e FROM EvaluatedResource e " + " WHERE e.session = "
				+ session.getId() + " AND e.resource = '" + adapt(resource) + "'");

		List<EvaluatedResource> result = query.list();
		if (result.isEmpty())
			return Optional.absent();
		return Optional.of(result.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<EvaluatedResource> getAllForSession(final EvaluationSession session, int page) {
		Query query = getSession()
				.createQuery("SELECT e FROM EvaluatedResource e WHERE e.session = " + session.getId())
				.setMaxResults(LIMIT).setFirstResult(page);

		Query countQuery = getSession()
				.createQuery("SELECT COUNT(*) FROM EvaluatedResource e WHERE e.session = " + session.getId());

		return new PaginatedResult<EvaluatedResource>(query.list(), page, (long) countQuery.uniqueResult(), LIMIT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EvaluatedResource> getAllForSession(final EvaluationSession session) {
		Query query = getSession()
				.createQuery("SELECT e FROM EvaluatedResource e " + " WHERE e.session = " + session.getId());

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getErroredForSession(final EvaluationSession session) {
		Query query = getSession().createQuery("SELECT e.resource FROM EvaluatedResource e " + " WHERE e.session = "
				+ session.getId() + " AND e.correct = false");

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCorrectForSession(final EvaluationSession session) {
		Query query = getSession().createQuery("SELECT e.resource FROM EvaluatedResource e " + " WHERE e.session = "
				+ session.getId() + " AND e.correct = true");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<EvaluatedResource> getAllPaginated(int page) {
		Query query = getSession().createQuery("SELECT e FROM EvaluatedResource e");
		Query countQuery = getSession().createQuery("SELECT COUNT(*) FROM EvaluatedResource");

		return new PaginatedResult<EvaluatedResource>(query.list(), page, (long) countQuery.uniqueResult(), LIMIT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EvaluatedResource> getAllRelated(String resource, IModel<EvaluationSession> sessionModel) {
		String query = "SELECT e FROM EvaluatedResource e WHERE e.resource = '" + adapt(resource) + "'";
		if (sessionModel.getObject() != null) query += " AND e.session.id <> " + sessionModel.getObject().getId();
		return getSession().createQuery(query).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EvaluatedResource> getAll() {
		return getSession().createQuery("SELECT e FROM EvaluatedResource e").list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getErrored() {
		return getSession().createQuery("SELECT e.resource FROM EvaluatedResource e WHERE e.correct = false").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCorrect() {
		return getSession().createQuery("SELECT e.resource FROM EvaluatedResource e WHERE e.correct = true").list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EvaluatedResource> getAllByCampaign(Campaign c) {
		return getSession().createQuery("SELECT e FROM EvaluatedResource e"
				+ " WHERE e.resource.session.campaign = " + c.getId()).list();
	}
	
	@Override
	public BigDecimal getSumScoreByCampaign(Campaign c) {
		BigDecimal result = (BigDecimal) getSession().createQuery("SELECT SUM(e.score) FROM EvaluatedResource e"
				+ " WHERE e.resource.session.campaign = " + c.getId()).list().get(0);

		return result == null ? BigDecimal.ZERO : result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getErroredByCampaign(Campaign c) {
		return getSession().createQuery("SELECT e.resource FROM EvaluatedResource e"
				+ " WHERE e.correct = false"
				+ " AND e.resource.session.campaign = " + c.getId()).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCorrectByCampaign(Campaign c) {
		return getSession().createQuery("SELECT e.resource FROM EvaluatedResource e"
				+ " WHERE e.correct = true"
				+ " AND e.resource.session.campaign = " + c.getId()).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<EvaluatedResource> getStatistic_1() {
		return getSession().createQuery(
				"select e FROM EvaluatedResource e"
				+ " where e.correct = false"
				+ " group by e.resource"
				+ " having count(distinct e.session.user) > 1").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<EvaluatedResource> getStatistic_2() {
		return getSession().createQuery(
				"select e FROM EvaluatedResource e"
				+ " where e.correct = true"
				+ " group by e.resource"
				+ " having count(distinct e.session.user) > 1").list();
	}
}
