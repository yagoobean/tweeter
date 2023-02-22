package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;

public class GetFollowingCountHandler extends BackgroundHandler<FollowObserver> {

    public GetFollowingCountHandler(FollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
        getObserver().updateFollowingCount(count);
    }
}
