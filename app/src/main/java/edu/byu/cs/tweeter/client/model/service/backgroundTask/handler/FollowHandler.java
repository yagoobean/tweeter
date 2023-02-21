package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

// FollowHandler

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends BackgroundHandler<FollowService.Observer> {
    private static final String TASK_KEY = "follow";

    private User selectedUser;
    private FollowService followService;

    public FollowHandler(FollowService.Observer observer, User selectedUser) {
        super(observer);
        this.selectedUser = selectedUser;
        followService = new FollowService();
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, getObserver());
        getObserver().updateFollowButton(false);
    }
}