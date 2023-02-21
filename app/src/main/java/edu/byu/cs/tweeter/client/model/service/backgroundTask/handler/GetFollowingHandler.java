package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowingHandler extends PagedUserHandler {

    public GetFollowingHandler(FollowService.Observer observer) {
        super(observer);
    }

}