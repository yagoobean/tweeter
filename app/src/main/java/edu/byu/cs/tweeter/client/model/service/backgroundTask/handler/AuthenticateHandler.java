package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticateHandler extends BackgroundHandler<UserService.Observer> {

    public AuthenticateHandler(UserService.Observer observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        User authenticatedUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

        // Cache user session information
        Cache.getInstance().setCurrUser(authenticatedUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        getObserver().handleSuccess(authenticatedUser, authToken);
    }
}
