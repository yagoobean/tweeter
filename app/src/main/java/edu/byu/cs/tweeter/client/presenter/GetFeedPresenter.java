package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter {
    public interface View {
        void setLoadingFooter(boolean value);
        void displayMessage(String message);
        void addMoreItems(List<Status> statuses);
        void getUser(User user);
    }

    private static final int PAGE_SIZE = 10;
    private final View view;
    private StatusService statusService;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private UserService userService;

    public GetFeedPresenter(View view) {
        this.view = view;
        this.statusService = new StatusService();
        this.userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            statusService.loadMoreFeeds(user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        }
    }

    public void executeUserTask(String userAlias) {
        // MOVE UP
        userService.executeUserTask(userAlias, new GetUserObserver());
    }

    // GETTERS AND SETTERS

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    //

    public class GetFeedObserver implements StatusService.Observer {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {     // fixme
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(EX_KEY + ex.getMessage());
        }

        @Override
        public void addItems(List<Status> statuses, boolean hasMorePages) {
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);

            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.addMoreItems(statuses);
        }

        @Override
        public void postStatus() {
            // not needed
        }
    }

    public class GetUserObserver implements UserService.Observer {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.getUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
