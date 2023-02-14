package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

// FollowHandler

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends Handler {
    private User selectedUser;
    private FollowService.Observer observer;
    private FollowService followService;

    public FollowHandler(FollowService.Observer observer, User selectedUser) {
        super(Looper.getMainLooper());
        this.observer = observer;
        this.selectedUser = selectedUser;
        followService = new FollowService();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
        if (success) {
            followService.updateSelectedUserFollowingAndFollowers(selectedUser, observer);
            // updateFollowButton(false);
            observer.updateFollowButton(false);
        } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
            // Toast.makeText(MainActivity.this, "Failed to follow: " + message, Toast.LENGTH_LONG).show();
            observer.displayError("Failed to follow: " + message);
        } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
            // Toast.makeText(MainActivity.this, "Failed to follow because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            observer.displayException(ex, "Failed to follow because of exception: ");
        }

        // followButton.setEnabled(true);
        // observer.updateFollowButton(true);
    }
}