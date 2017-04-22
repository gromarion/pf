package com.itba.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "error")
public class Error extends PersistentEntity {

    @Column(name = "title")
    private String name;

    @Column(name = "example")
    private String example;

    @Column(name = "description")
    private String description;

    Error() {}

    public String getName() {
        return name;
    }

    public String getExample() {
        return example;
    }

    public String getDescription() {
        return description;
    }
}
