package edu.byu.cs.tweeter.client.model.service;

public interface BackgroundObserver {
    static final String TASK_KEY = "";
    static final String EX_KEY = "Failed to " + TASK_KEY + " because of exception: ";

    void displayError(String message);
    void displayException(Exception ex);

}
