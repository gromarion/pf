package com.itba.domain.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "users")
public class User extends PersistentEntity {

	private String fullName;
	
    private String username;
	
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<EvaluationSession> sessions;

    User(){}

    public User(String fullName, String name, String password) {
        this.fullName = fullName;
    	this.username = name;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public String getConfirmedPassword() {
		return password;
	}

	public String getFullName() {
		return fullName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Set<EvaluationSession> getSessions() {
		return sessions;
	}
}
