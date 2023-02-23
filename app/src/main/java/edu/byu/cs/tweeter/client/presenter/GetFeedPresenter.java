package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter extends PagedPresenter<Status> {
    public interface View extends PagedView<Status> {

        void addMoreItems(List<Status> statuses);
        void getUser(User user);
    }

    // private final View view;
    private StatusService statusService;

    public GetFeedPresenter(View view) {
        super(view);
        this.statusService = new StatusService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            statusService.loadMoreFeeds(user, PAGE_SIZE, lastItem, new GetFeedObserver());
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
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    //

    public class GetFeedObserver extends PagedObserver {

        @Override
        public String getTaskName() {
            return "get feed";
        }

        @Override
        protected void extra() {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }

    }

//    public class GetUserObserver extends PagedObserver {
//        @Override
//        protected String getTaskName() {
//            return "get user's profile";
//        }
//
//        @Override
//        protected void extra() {
//            // do nothing
//        }
//    }
}
