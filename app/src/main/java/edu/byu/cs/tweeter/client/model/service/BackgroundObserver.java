package edu.byu.cs.tweeter.client.model.service;

public interface BackgroundObserver {

    void displayError(String message);
    void displayException(Exception ex);
    String getTaskName();

}
