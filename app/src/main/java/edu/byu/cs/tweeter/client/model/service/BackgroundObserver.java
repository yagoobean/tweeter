package edu.byu.cs.tweeter.client.model.service;

public abstract interface BackgroundObserver {
    public static final String TASK_KEY = "";
    public static final String EX_KEY = "Failed to " + TASK_KEY + " because of exception: ";

    public abstract void displayError(String message);
    public abstract void displayException(Exception ex);

}
