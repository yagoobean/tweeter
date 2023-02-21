package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

// PostStatusHandler

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.StatusService;

public class PostStatusHandler extends BackgroundHandler<StatusService.Observer> {
    private static final String TASK_KEY = "post";

    public PostStatusHandler(StatusService.Observer observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        getObserver().postStatus();
    }
}