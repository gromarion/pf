package com.itba.web.page;

import com.itba.web.WicketSession;
import org.apache.wicket.markup.html.WebPage;

@SuppressWarnings("serial")
public class BasePage extends WebPage {

    public BasePage() {
        WicketSession session = getAppSession();
        if (!session.isSignedIn()) {
            redirectToInterceptPage(new LoginPage());
        }
    }

    protected WicketSession getAppSession() {
        return (WicketSession) getSession();
    }
}
