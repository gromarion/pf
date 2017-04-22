package com.itba.domain.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

import lombok.Getter;

@Entity
@Table(name = "evaluation_session")
public class EvaluationSession extends PersistentEntity {

    @ManyToOne
    private @Getter Campaign campaign;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "session")
    private Set<EvaluatedResource> evaluatedResources;

    EvaluationSession() {}

    public EvaluationSession(Campaign campaign, User user) {
        this.campaign = campaign;
        this.user = user;
    }
}
