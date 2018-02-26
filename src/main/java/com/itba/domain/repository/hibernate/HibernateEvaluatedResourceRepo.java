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
		String queryString = String.format("SELECT e FROM EvaluatedResource e WHERE e.session = %s AND e.resource = '%s'",
				session.getId(),
				adapt(resource)); 
		Query query = getSession().createQuery(queryString);
		List<EvaluatedResource> result = query.list();

		return result.isEmpty() ? Optional.absent() : Optional.of(result.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<EvaluatedResource> getAllForSession(final EvaluationSession session, int page) {
		String queryString = String.format(
				"SELECT e FROM EvaluatedResource e WHERE e.session = %s order by timestamp DESC",
				session.getId());
		Query query = getSession().createQuery(queryString)
				.setMaxResults(LIMIT)
				.setFirstResult(page * LIMIT);
		String countQueryString = String.format(
				"SELECT COUNT(*) FROM EvaluatedResource e WHERE e.session = %s",
				session.getId()); 
		Query countQuery = getSession().createQuery(countQueryString);

		return new PaginatedResult<EvaluatedResource>(query.list(), page, (long) countQuery.uniqueResult(), LIMIT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EvaluatedResource> getAllForSession(final EvaluationSession session) {
		String queryString = String.format(
				"SELECT e FROM EvaluatedResource e WHERE e.session = %s order by timestamp DESC",
				session.getId());
		Query query = getSession().createQuery(queryString);

		return query.list();
	}

	@Override
	public List<String> getErroredForSession(final EvaluationSession session) {
		return getForSession(session, false);
	}
	
	@Override
	public List<String> getCorrectForSession(final EvaluationSession session) {
		return getForSession(session, true);
	}

	@SuppressWarnings("unchecked")
	private List<String> getForSession(final EvaluationSession session, boolean correct) {
		String queryString = String.format(
				"SELECT e.resource FROM EvaluatedResource e WHERE e.session = %s AND e.correct = %s",
				session.getId(),
				correct);
		Query query = getSession().createQuery(queryString);

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
		String query = String.format(
				"SELECT e FROM EvaluatedResource e WHERE e.resource.session.campaign = %s",
				c.getId());
		return getSession().createQuery(query).list();
	}
	
	@Override
	public BigDecimal getSumScoreByCampaign(Campaign c) {
		String query = String.format(
				"SELECT SUM(e.score) FROM EvaluatedResource e WHERE e.resource.session.campaign = %s AND e.score >= 0",
				c.getId());
		BigDecimal result = (BigDecimal) getSession().createQuery(query).list().get(0);

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
