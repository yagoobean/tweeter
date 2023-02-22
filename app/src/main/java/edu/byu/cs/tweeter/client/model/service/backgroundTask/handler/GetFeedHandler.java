package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.ItemObserver;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends PagedStatusHandler {
    private static final String TASK_KEY = "get feed";

    public GetFeedHandler(ItemObserver<Status> observer) {
        super(observer);
    }
}