package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "classes")
public class Clazz extends PersistentEntity {

    @Column(name = "curi")
    private String uri;

    @Column(name = "cname")
    private String name;

    @Column(name = "cparent")
    private BigInteger parent;

    @Column(name = "count_cache")
    private BigInteger countCache;

    @Column(name = "is_leaf")
    private Boolean isLeaf;

    Clazz() {}
}
