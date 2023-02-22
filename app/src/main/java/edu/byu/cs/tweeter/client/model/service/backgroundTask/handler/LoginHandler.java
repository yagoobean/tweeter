package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;

public class LoginHandler extends AuthenticateHandler {
    private static final String TASK_KEY = "login";

    public LoginHandler(AuthenticatedObserver observer) {
        super(observer);
    }

}
