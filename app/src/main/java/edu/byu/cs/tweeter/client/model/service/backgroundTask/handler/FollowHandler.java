package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

// FollowHandler

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends FollowStatusHandler {
    private static final String TASK_KEY = "follow";

    private User selectedUser;
    private FollowService followService;

    public FollowHandler(FollowObserver observer, User selectedUser) {
        super(observer, selectedUser);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        super.handleSuccess(msg);
        getObserver().updateFollowButton(false);
    }
}