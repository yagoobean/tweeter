package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowStatusHandler extends BackgroundHandler<FollowService.Observer> {

    protected User selectedUser;
    protected FollowService followService;

    public FollowStatusHandler(FollowService.Observer observer, User selectedUser) {
        super(observer);
        this.selectedUser = selectedUser;
        followService = new FollowService();
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, getObserver());
    }
}
