package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.BackgroundObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;

public abstract class BackgroundHandler<T extends BackgroundObserver> extends Handler {
    protected static final String TASK_KEY = "";
    protected static final String FAIL_KEY = "Failed to " + TASK_KEY + ": ";

    private T observer;

    public BackgroundHandler(T observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);

        if (success) {

            handleSuccess(msg);

        } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
            observer.displayError(FAIL_KEY + message);
        } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
            observer.displayException(ex);
        }
    }

    protected abstract void handleSuccess(@NonNull Message msg);

    public T getObserver() {
        return observer;
    }
}
