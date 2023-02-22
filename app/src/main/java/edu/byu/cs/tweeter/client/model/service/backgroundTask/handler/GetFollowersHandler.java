package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.ItemObserver;

public class GetFollowersHandler extends PagedUserHandler {

    public GetFollowersHandler(ItemObserver observer) {
        super(observer);
    }

}
