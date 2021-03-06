package com.itba.domain.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "evaluation_session")
public class EvaluationSession extends PersistentEntity implements Serializable {
    @ManyToOne
    private Campaign campaign;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "session")
    private Set<EvaluatedResource> evaluatedResources;

    EvaluationSession() {}

    public EvaluationSession(Campaign campaign, User user) {
        this.campaign = campaign;
        this.user = user;
    }

	public Campaign getCampaign() {
		return campaign;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public Set<EvaluatedResource> getEvaluatedResources() {
		return this.evaluatedResources;
	}
}
