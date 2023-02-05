package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter {
    private static final int PAGE_SIZE = 10;

    public interface View {

        void setLoadingFooter(boolean val);
        void displayMessage(String message);
        void addMoreItems(List<User> followers);

    }

    private boolean isLoading = false;
    private View view;
    private User lastFollower;
    private FollowService followService;
    private boolean hasMorePages;

    public GetFollowersPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreFollowers(user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
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

    private class GetFollowersObserver implements FollowService.Observer {
        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex, String header) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(header + ex.getMessage());
        }

        @Override
        public void addItems(List<User> followers, boolean hasMorePages) {
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);

            isLoading = false;
            view.setLoadingFooter(false);
            view.addMoreItems(followers);
        }

        @Override
        public void updateFollowersCount(int count) {
            // don't need
        }

        @Override
        public void updateFollowingCount(int count) {
            // don't need
        }

        @Override
        public void setFollowers(boolean isFollower) {
            // don't need
        }

        @Override
        public void updateFollowButton(boolean val) {
            // don't need
        }
    }
}
