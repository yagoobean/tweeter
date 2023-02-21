package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;

public class GetFollowingCountHandler extends Handler {
    private FollowService.Observer observer;

    public GetFollowingCountHandler(FollowService.Observer observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
        if (success) {
            int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
            // followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
            observer.updateFollowingCount(count);
        } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
            // Toast.makeText(MainActivity.this, "Failed to get following count: " + message, Toast.LENGTH_LONG).show();
            observer.displayError("Failed to get following count: " + message);
        } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
            // Toast.makeText(MainActivity.this, "Failed to get following count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            observer.displayException(ex);
        }
    }
}
