package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginHandler extends Handler {

    UserService.LoginObserver observer;

    public LoginHandler(UserService.LoginObserver observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
        if (success) {
            User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            observer.handleSuccess(loggedInUser, authToken);

        } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
            observer.handleFailure("Failed to login: " + message);
        } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
