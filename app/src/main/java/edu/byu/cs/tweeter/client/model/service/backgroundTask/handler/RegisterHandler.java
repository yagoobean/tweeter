package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;

public class RegisterHandler extends AuthenticateHandler {
    private static final String TASK_KEY = "register";

    public RegisterHandler(AuthenticatedObserver observer) {
        super(observer);
    }

}
