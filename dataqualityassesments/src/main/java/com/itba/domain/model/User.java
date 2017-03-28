package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends PersistentEntity {

    private String name;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<EvaluationSession> sessions;

    User(){}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<EvaluationSession> getSessions() {
        return sessions;
    }
}
