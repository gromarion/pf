package com.itba.domain.model;

import com.itba.domain.PersistentEntity;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "evaluation_session")
public class EvaluationSession extends PersistentEntity {

    @ManyToOne
//    @Column(name = "cid")
    private Campaign campaign;

    @ManyToOne
//    @Column(name = "uid")
    private User user;

    @Column(name = "timestamp")
    private DateTime timestamp;

    @OneToMany(mappedBy = "session")
    private Set<EvaluatedResource> evaluatedResources;

    EvaluationSession() {}
}
