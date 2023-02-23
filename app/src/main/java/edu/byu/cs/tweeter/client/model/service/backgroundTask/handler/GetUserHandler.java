package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.presenter.GetFeedPresenter;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends BackgroundHandler<PagedPresenter.GetUserObserver> {

    public GetUserHandler(GetFeedPresenter.GetUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(@NonNull Message msg) {

        User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
        getObserver().postStatus(user, null);
    }
}