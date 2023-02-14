package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowHandler extends Handler {
    private FollowService.Observer observer;
    private User selectedUser;
    private FollowService followService;

    public UnfollowHandler(FollowService.Observer observer, User selectedUser) {
        super(Looper.getMainLooper());
        this.observer = observer;
        this.selectedUser = selectedUser;
        this.followService = new FollowService();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
        if (success) {
            followService.updateSelectedUserFollowingAndFollowers(selectedUser, observer);
            observer.updateFollowButton(true);
        } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
            observer.displayError("Failed to unfollow: " + message);
        } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
            observer.displayException(ex, "Failed to unfollow because of exception: ");
        }

    }
}