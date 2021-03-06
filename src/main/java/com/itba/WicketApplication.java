package com.itba;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.ExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itba.common.HibernateRequestCycleListener;
import com.itba.domain.model.UserRolesAuthorizer;
import com.itba.web.WicketSession;
import com.itba.web.page.ErrorPage;
import com.itba.web.page.LoginPage;

@Component
public class WicketApplication extends WebApplication {

	private final SessionFactory sessionFactory;

	@Autowired
	public WicketApplication(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Class<? extends WebPage> getHomePage() {
		return LoginPage.class;
	}

	@Override
	public void init() {
		super.init();
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRolesAuthorizer()));
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		getRequestCycleListeners().add(new HibernateRequestCycleListener(sessionFactory));
		
		getApplicationSettings().setPageExpiredErrorPage(LoginPage.class);
		getApplicationSettings().setAccessDeniedPage(LoginPage.class);
		getApplicationSettings().setInternalErrorPage(ErrorPage.class);
		
		getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WicketSession(request);
	}
	
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		// TODO: pasar a DEPLOYMENT antes de entregar
		return RuntimeConfigurationType.DEPLOYMENT;
	}
}
