package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "errors")
public class Error extends PersistentEntity {

    @Column(name = "error_title")
    private String title;

    @Column(name = "example_uri")
    private String exampleUri;

    @Column(name = "example_n3")
    private String exampleN3;

    @Column(name = "description")
    private String description;

    @Column(name = "error_parent")
    private BigInteger errorParent;

    @Column(name = "is_leaf")
    private Boolean isLeaf;

    Error() {}
}
