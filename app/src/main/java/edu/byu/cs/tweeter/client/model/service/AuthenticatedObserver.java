package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticatedObserver extends BackgroundObserver {

    void postStatus(User user, AuthToken authToken);
}
