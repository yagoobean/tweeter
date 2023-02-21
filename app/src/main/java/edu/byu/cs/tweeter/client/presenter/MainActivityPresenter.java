package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainActivityPresenter {
    public interface View {
        void updateFollowersCount(int count);
        void updateFollowingCount(int count);
        void displayMessage(String message);
        void logOut();
        void setFollowers(boolean isFollower);
        void updateFollowButton(boolean val);
        void postStatus();
    }

    private View view;
    private FollowService followService;
    private UserService userService;
    private StatusService statusService;

    public MainActivityPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
        this.statusService = new StatusService();
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, new FollowObserver());
    }

    public void executeIsFollowerTask(User selectedUser) {
        followService.executeIsFollowerTask(selectedUser, new FollowObserver());
    }

    public void executeFollowTask(User selectedUser) {
        followService.executeFollowTask(selectedUser, new FollowObserver());
    }

    public void executeUnfollowTask(User selectedUser) {
        followService.executeUnfollowTask(selectedUser, new FollowObserver());
    }

    public void executeStatusTask(String post) throws ParseException {
        statusService.executeStatusTask(post, new StatusObserver());
    }

    public void logOut() {
        userService.logout(new LogoutObserver());
    }

    private class FollowObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage(EX_KEY + ex.getMessage());  
        }

        @Override
        public void addItems(List<User> items, boolean hasMorePages) {
            // don't think I need this
        }

        @Override
        public void updateFollowersCount(int count) {
            view.updateFollowersCount(count);
        }

        @Override
        public void updateFollowingCount(int count) {
            view.updateFollowingCount(count);
        }

        @Override
        public void setFollowers(boolean isFollower) {
            view.setFollowers(isFollower);
        }

        @Override
        public void updateFollowButton(boolean val) {
            view.updateFollowButton(val);
        }
    }

    private class LogoutObserver implements UserService.Observer {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.logOut();
        }

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }

    private class StatusObserver implements StatusService.Observer {
        private static final String TASK_KEY = "post status";

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to " + TASK_KEY + " because of exception: " + ex.getMessage());
        }

        @Override
        public void addItems(List<Status> items, boolean hasMorePages) {
            // NOT NEEDED
        }

        @Override
        public void postStatus() {
            view.postStatus();
        }
    }
}
