package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.BackgroundObserver;

public abstract class BackgroundPresenter<T extends BackgroundView> {
    protected T view;

    public BackgroundPresenter(T view) {
        this.view = view;

    }

    public abstract class BaseObserver implements BackgroundObserver {
        @Override
        public void displayError(String message) {
            // TODO
        }

        @Override
        public void displayException(Exception ex) {
            // TODO
        }
    }

}
