package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.StatusService;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends PagedStatusHandler {
    private static final String TASK_KEY = "get story";

    public GetStoryHandler(StatusService.Observer observer) {
        super(observer);
    }

}