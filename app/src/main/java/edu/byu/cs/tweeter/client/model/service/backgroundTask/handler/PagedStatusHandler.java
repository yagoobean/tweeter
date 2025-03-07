package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.ItemObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.model.domain.Status;

public class PagedStatusHandler extends PagedHandler<ItemObserver<Status>> {

    public PagedStatusHandler(ItemObserver<Status> observer) {
        super(observer);
    }

    @Override
    protected void manageItems(@NonNull Message msg) {
        List<Status> statuses = (List<Status>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
        getObserver().addItems(statuses, hasMorePages);
    }
}
