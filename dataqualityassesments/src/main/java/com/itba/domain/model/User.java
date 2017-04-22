package com.itba.domain.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User extends PersistentEntity {

	private @Getter @Setter String fullName;
	
    private @Getter @Setter String username;
	
    private @Getter @Setter String password;

	@Getter
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
}
