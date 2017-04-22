package com.itba.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

import lombok.Getter;

@Entity
@Table(name = "error")
public class Error extends PersistentEntity {

    @Column(name = "name")
    private @Getter String name;

    @Column(name = "example")
    private @Getter String example;

    @Column(name = "description")
    private @Getter String description;

    Error() {}
}
