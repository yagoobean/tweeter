package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedUserHandler extends PagedHandler<FollowService.Observer>{

    public PagedUserHandler(FollowService.Observer observer) {
        super(observer);
    }

    @Override
    protected void manageItems(@NonNull Message msg) {
        List<User> followees = (List<User>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
        getObserver().addItems(followees, hasMorePages);
    }
}
