package com.itba.domain.repository.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itba.domain.model.Campaign;
import com.itba.domain.model.Error;
import com.itba.domain.model.EvaluatedResource;
import com.itba.domain.model.EvaluatedResourceDetail;
import com.itba.domain.repository.EvaluatedResourceDetailRepo;

@Repository
public class HibernateEvaluatedResourceDetailRepo extends AbstractHibernateRepo
		implements EvaluatedResourceDetailRepo, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	public HibernateEvaluatedResourceDetailRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<EvaluatedResourceDetail> getPreviousErrors(final EvaluatedResource resource, String predicate, String object) {
		List<EvaluatedResourceDetail> result = find(
				"select detail from EvaluatedResourceDetail detail "
				+ " where detail.resource = ? "
				+ " and detail.predicate = ? "
				+ " and detail.object = ? ",
				resource, predicate, object);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAlreadyEvaluatedForResource(final EvaluatedResource resource) {
		Query query = getSession()
				.createQuery("SELECT distinct concat(d.predicate, d.object) FROM EvaluatedResourceDetail d "
						+ " WHERE d.resource = " + resource.getId());

		return query.list();
	}

	@Override
	public Long getQtyByError(final Error error) {
		Query query = getSession()
				.createQuery("SELECT count(*) FROM EvaluatedResourceDetail d "
						+ " WHERE d.error = " + error.getId());

		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long getQtyByErrorAndCampaign(final Error error, final Campaign campaign) {
		// XXX: se hace el GROUP BY para evitar contar varias veces un mismo error en un mismo documento marcado por distintos usuarios
		Query query = getSession()
				.createQuery("SELECT d.resource.session.campaign.id, d.resource.resource, d.predicate, d.object, d.error.id"
						+ " FROM EvaluatedResourceDetail d "
						+ " WHERE d.error = " + error.getId()
						+ " AND d.resource.session.campaign = " + campaign.getId()
						+ " GROUP BY d.resource.session.campaign.id, d.resource.resource, d.predicate, d.object, d.error.id");
		return new Long(query.list().size());
	}
}
