package com.itba.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;

import com.itba.domain.PersistentEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User extends PersistentEntity implements Serializable {
	
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String EVALUATOR_ROLE = "EVALUATOR";
	public static final String GUEST_ROLE = "GUEST";

	private String fullName;
	private String username;
	private String password;
	private String roles;
	private Timestamp deleted_at;
	@OneToMany(mappedBy = "user")
	private Set<EvaluationSession> sessions;

	User() {
	}

	public User(String fullName, String name, String password, String roles) {
		this.fullName = fullName;
		this.username = name;
		this.password = password;
		this.roles = roles;
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
		return new Roles(roles).hasRole(role);
	}

	public boolean hasAnyRole(Roles roles) {
		return new Roles(this.roles).hasAnyRole(roles);
	}
	
	public boolean isDeleted() {
		return deleted_at != null;
	}
}
