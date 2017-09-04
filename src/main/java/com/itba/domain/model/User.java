package com.itba.domain.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "users")
public class User extends PersistentEntity {

	private String fullName;

	private String username;

	private String password;

	private Roles roles = null;

	@OneToMany(mappedBy = "user")
	private Set<EvaluationSession> sessions;

	User() {
	}

	public User(String fullName, String name, String password, String roles) {
		this.fullName = fullName;
		this.username = name;
		this.password = password;
		this.roles = new Roles(roles);
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

	public boolean hasRole(String role) {
		return roles.hasRole(role);
	}

	public boolean hasAnyRole(Roles roles) {
		return this.roles.hasAnyRole(roles);
	}
}
