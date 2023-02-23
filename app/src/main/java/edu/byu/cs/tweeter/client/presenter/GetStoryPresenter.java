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

public class GetStoryPresenter extends PagedPresenter<Status> {
    public interface View extends PagedView<Status> {
        void addMoreItems(List<Status> statuses);
        void getUser(User user);
    }

    private StatusService statusService;

    public GetStoryPresenter(View view) {
        super(view);
        this.statusService = new StatusService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(isLoading);
            statusService.loadMoreStories(user, PAGE_SIZE, lastItem, new GetStoryObserver());
        }
    }

    public void executeUserTask(String userAlias) {
        userService.executeUserTask(userAlias, new GetUserObserver());
    }

    // GETTERS AND SETTERS
    public boolean isLoading() {
        return isLoading;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    //

    public class GetStoryObserver extends PagedObserver {

        @Override
        protected String getTaskName() {
            return "get story";
        }

        @Override
        protected void extra() {
            isLoading = false;
            view.setLoadingFooter(isLoading);

        }

    }
}
