package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.BackgroundObserver;
import edu.byu.cs.tweeter.client.model.service.ItemObserver;
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

    private abstract class Observer implements BackgroundObserver {
        protected abstract void extra();

        @Override
        public void displayError(String message) {
            extra();
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception exception) {
            extra();
            view.displayMessage(EX_KEY + exception.getMessage());
        }
    }

    public class GetStoryObserver extends Observer implements ItemObserver<Status> {
        private static final String TASK_KEY = "get story";

        @Override
        protected void extra() {
            isLoading = false;
            view.setLoadingFooter(isLoading);

        }

        @Override
        public void addItems(List<Status> statuses, boolean hasMorePages) {
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);

            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.addMoreItems(statuses);
        }
    }

    private class GetUserObserver extends Observer implements AuthenticatedObserver {
        private static final String TASK_KEY = "get user's profile";

        @Override
        public void postStatus(User user, AuthToken authToken) {
            view.getUser(user);
        }

        @Override
        protected void extra() {
            // leave empty
        }
    }
}
