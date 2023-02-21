package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.BackgroundObserver;

public abstract class PagedHandler<T extends BackgroundObserver> extends BackgroundHandler<T> {

    public PagedHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {
        manageItems(msg);
    }

    protected abstract void manageItems(@NonNull Message msg);
}
