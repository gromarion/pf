package com.itba.domain.model;

import com.itba.domain.PersistentEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "evaluation_session")
public class EvaluationSession extends PersistentEntity {

    @ManyToOne
    private Campaign campaign;

    @ManyToOne
    private User user;

    @Column(name = "timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @DateTimeFormat(style="yyyy-MM-dd'T'HH:mm:ss")
    private DateTime timestamp;

    @OneToMany(mappedBy = "session")
    private Set<EvaluatedResource> evaluatedResources;

    EvaluationSession() {}

    public EvaluationSession(Campaign campaign, User user, DateTime dateTime) {
        this.campaign = campaign;
        this.user = user;
        this.timestamp = dateTime;
    }
}
