package com.itba.web;

import com.itba.domain.EntityModel;
import com.itba.domain.UserRepo;
import com.itba.domain.model.Campaign;
import com.itba.domain.model.User;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

@SuppressWarnings("serial")
public class WicketSession extends WebSession {

    private IModel<User> user = new EntityModel<User>(User.class);
    private IModel<Campaign> campaign = new EntityModel<Campaign>(Campaign.class);

    public static WicketSession get() {
        return (WicketSession) Session.get();
    }

    public WicketSession(Request request) {
        super(request);
    }

    public User getUser() {
        return (user != null) ? user.getObject() : null;
    }

    public Campaign getCampaign() {
        return (campaign != null) ? campaign.getObject() : null;
    }

    public boolean signIn(String name, String password, Campaign campaign, UserRepo users) {
        if (this.user.getObject() != null)
            return true;
        User user = users.getByName(name);
        if (user != null && user.checkPassword(password)) {
            this.user.setObject(user);
            this.campaign.setObject(campaign);
            return true;
        }
        return false;
    }

    public boolean isSignedIn() {
        return user.getObject() != null;
    }

    public void signOut() {
        invalidate();
        clear();
    }

    @Override
    public void detach() {
        super.detach();
        if (user != null) user.detach();
        if (campaign != null) campaign.detach();
    }
}
