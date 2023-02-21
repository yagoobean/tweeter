package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryPresenter {
    public interface View {
        void setLoadingFooter(boolean value);
        void displayMessage(String message);
        void addMoreItems(List<Status> statuses);
        void getUser(User user);
    }

    private static final int PAGE_SIZE = 10;
    private View view;
    private StatusService statusService;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private UserService userService;

    public GetStoryPresenter(View view) {
        this.view = view;
        this.statusService = new StatusService();
        this.userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(isLoading);
            statusService.loadMoreStories(user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }

    public void executeUserTask(String userAlias) {
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

    public class GetStoryObserver implements StatusService.Observer {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());

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

    private class GetUserObserver implements UserService.Observer {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.getUser(user);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception exception) {
            view.displayMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
