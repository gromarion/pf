package com.itba.domain.model;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;

import com.itba.web.WicketSession;

public class UserRolesAuthorizer implements IRoleCheckingStrategy {

	public UserRolesAuthorizer() {
	}

	@Override
	public boolean hasAnyRole(Roles roles) {
		WicketSession authSession = (WicketSession) Session.get();
		return authSession.getUser().hasAnyRole(roles);
	}

}
