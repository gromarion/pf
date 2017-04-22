package com.itba.domain.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "evaluated_resource")
public class EvaluatedResource extends PersistentEntity {

    @ManyToOne
    private @Getter EvaluationSession session;

    @Column(name = "resource")
    private @Getter String resource;

    @Column(name = "comments")
    private @Getter @Setter String comments;

    @OneToMany(mappedBy = "resource")
    private @Getter Set<EvaluatedResourceDetail> details;

    EvaluatedResource() {}

    public EvaluatedResource(EvaluationSession session, String resource) {
        this.session = session;
        this.resource = resource;
    }
}
