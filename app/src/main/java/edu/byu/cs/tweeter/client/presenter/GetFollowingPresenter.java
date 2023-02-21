package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter {

    private static final int PAGE_SIZE = 10;

    public interface View {
        void setLoadingFooter(boolean value);
        void displayMessage(String message);

        void addMoreItems(List<User> followees);
        void getUser(User user);
    }
    private FollowService followService;

    private boolean isLoading = false;

    private boolean hasMorePages;

    private User lastFollowee;

    private View view;
    private UserService userService;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public GetFollowingPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreFollowees(user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }
    }

    public void executeUserTask(String userAlias) {
        userService.executeUserTask(userAlias, new GetUserObserver());
    }

    public class GetFollowingObserver implements FollowService.Observer {

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
            view.displayMessage("Failed to get following because of error: " + ex.getMessage());
        }

        @Override
        public void addItems(List<User> followees, boolean hasMorePages) {
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);

            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.addMoreItems(followees);
        }

        @Override
        public void updateFollowersCount(int count) {
            // not needed
        }

        @Override
        public void updateFollowingCount(int count) {
            // not needed
        }

        @Override
        public void setFollowers(boolean isFollower) {
            // not needed
        }

        @Override
        public void updateFollowButton(boolean val) {
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
