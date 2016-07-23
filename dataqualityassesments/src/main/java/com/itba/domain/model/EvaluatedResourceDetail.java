package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "evaluated_resource_details")
public class EvaluatedResourceDetail extends PersistentEntity {

//    @Column(name = "rid")
    @ManyToOne
    private EvaluatedResource resource;

    @Column(name = "predicate")
    private String predicate;

    @Column(name = "object")
    private String object;

    @Column(name = "error_id")
    private BigInteger errorId;

    @Column(name = "comment")
    private String comment;
}
