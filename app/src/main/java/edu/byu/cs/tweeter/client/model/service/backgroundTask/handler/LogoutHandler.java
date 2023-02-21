package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.UserService;

// LogoutHandler

public class LogoutHandler extends BackgroundHandler<UserService.Observer> {
    private static final String TASK_KEY = "logout";

    public LogoutHandler(UserService.Observer observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        getObserver().handleSuccess(null, null); // FIXME?
    }
}
