package com.itba.domain.model;

import com.itba.domain.PersistentEntity;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "evaluation_session")
public class EvaluationSession extends PersistentEntity {

    @Column(name = "cid")
    private Campaign campaign;

    @Column(name = "uid")
    private User user;

    @Column(name = "timestamp")
    private DateTime timestamp;

    EvaluationSession() {}
}
