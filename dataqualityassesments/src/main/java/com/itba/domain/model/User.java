package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

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

    public String getUsername() {
        return username;
    }
    
    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }
    
    public String getConfirmedPassword() {
		return password;
	}

    public Set<EvaluationSession> getSessions() {
        return sessions;
    }
    
    public void setFullName(String fullName) {
    	this.fullName = fullName;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
}
