package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

// PostStatusHandler

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;

public class PostStatusHandler extends Handler {
    private StatusService.Observer observer;

    public PostStatusHandler(StatusService.Observer observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
        if (success) {
            // postingToast.cancel();
            // Toast.makeText(MainActivity.this, "Successfully Posted!", Toast.LENGTH_LONG).show();
            observer.postStatus();
        } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
            // Toast.makeText(MainActivity.this, "Failed to post status: " + message, Toast.LENGTH_LONG).show();
            observer.displayError("Failed to post status: " + message);
        } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
            // Toast.makeText(MainActivity.this, "Failed to post status because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            observer.displayException(ex);
        }
    }
}