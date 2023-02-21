package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginHandler extends AuthenticateHandler {
    private static final String TASK_KEY = "login";

    public LoginHandler(UserService.Observer observer) {
        super(observer);
    }

}
