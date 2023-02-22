package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface ItemObserver<T> extends BackgroundObserver{
    void addItems(List<T> items, boolean hasMorePages);
}
