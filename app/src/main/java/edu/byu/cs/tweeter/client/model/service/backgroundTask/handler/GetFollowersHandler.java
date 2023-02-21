package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowersHandler extends PagedUserHandler {

    public GetFollowersHandler(FollowService.Observer observer) {
        super(observer);
    }

}
