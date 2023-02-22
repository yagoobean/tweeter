package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowHandler extends FollowStatusHandler {
    protected static final String TASK_KEY = "unfollow";

    public UnfollowHandler(FollowObserver observer, User selectedUser) {
        super(observer, selectedUser);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        super.handleSuccess(msg);
        getObserver().updateFollowButton(true);
    }
}