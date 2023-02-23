package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.BackgroundObserver;

public abstract class BackgroundPresenter<T extends BackgroundView> {
    protected T view;

    public BackgroundPresenter(T view) {
        this.view = view;

    }

    public abstract class BaseObserver implements BackgroundObserver {

        protected abstract void extra();

        @Override
        public void displayError(String message) {
            extra();
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            extra();
            view.displayMessage("Failed to " + getTaskName() + " because of exception: " + ex.getMessage());
        }
    }

}
