package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends PersistentEntity {

    @Column(name = "googleid")
    private String googleId;
    private String name;
    private String picture;
    private String profile;
    private Integer statr;
    private Integer statt;
    private Integer statd;

    User(){}

    public User(String googleId, String name, String picture, String profile) {
        this.googleId = googleId;
        this.name = name;
        this.picture = picture;
        this.profile = profile;
    }


}
