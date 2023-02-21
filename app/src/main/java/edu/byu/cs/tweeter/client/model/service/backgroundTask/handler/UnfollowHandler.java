package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

// TODO: potentially merge with followHandler (have a base class)
public class UnfollowHandler extends BackgroundHandler<FollowService.Observer> {
    protected static final String TASK_KEY = "unfollow";

    private User selectedUser;
    private FollowService followService;

    public UnfollowHandler(FollowService.Observer observer, User selectedUser) {
        super(observer);
        this.selectedUser = selectedUser;
        followService = new FollowService();
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, getObserver());
        getObserver().updateFollowButton(true);
    }
}