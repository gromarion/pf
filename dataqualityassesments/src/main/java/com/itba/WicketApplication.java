package com.itba;

import com.itba.common.HibernateRequestCycleListener;
import com.itba.web.page.LoginPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WicketApplication extends WebApplication
{

	private final SessionFactory sessionFactory;

	@Autowired
	public WicketApplication(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return LoginPage.class;
	}

	@Override
	public void init()
	{
		super.init();
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		getRequestCycleListeners().add(new HibernateRequestCycleListener(sessionFactory));

	}
}
