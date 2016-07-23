package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "campaign")
public class Campaign extends PersistentEntity {

    @Column(name = "cname")
    private String name;

    @Column(name = "cendpoint")
    private String endpoint;

    @Column(name = "cgraphs")
    private String graphs;

    @Column(name = "copened")
    private Integer opened;

    @OneToMany(mappedBy = "campaign")
    private Set<EvaluationSession> sessions;

    Campaign() {}


}
