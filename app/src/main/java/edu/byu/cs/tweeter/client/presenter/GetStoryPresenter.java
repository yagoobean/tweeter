package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryPresenter {
    public interface View {
        void setLoadingFooter(boolean value);
        void displayMessage(String message);
        void addMoreItems(List<Status> statuses);
    }

    private static final int PAGE_SIZE = 10;
    private View view;  // TODO does this need to be final?
    private StatusService statusService;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public GetStoryPresenter(View view) {
        this.view = view;
        this.statusService = new StatusService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(isLoading);
            statusService.loadMoreStories(user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }

    public void executeUserTask(String userAlias) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserService.GetUserHandler());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
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
}
