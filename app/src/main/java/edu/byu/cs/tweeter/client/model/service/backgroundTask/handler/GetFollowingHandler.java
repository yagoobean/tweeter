package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.ItemObserver;

public class GetFollowingHandler extends PagedUserHandler {

    public GetFollowingHandler(ItemObserver observer) {
        super(observer);
    }

}