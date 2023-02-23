package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.BackgroundObserver;
import edu.byu.cs.tweeter.client.model.service.FollowObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.SimpleObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainActivityPresenter extends BackgroundPresenter<MainActivityPresenter.View>  {
    public interface View extends BackgroundView {
        void updateFollowersCount(int count);
        void updateFollowingCount(int count);
        void displayMessage(String message);
        void logOut();
        void setFollowers(boolean isFollower);
        void updateFollowButton(boolean val);
        void postStatus();
    }

    private FollowService followService;
    private UserService userService;
    private StatusService statusService;

    public MainActivityPresenter(View view) {
        super(view);
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

    private class FollowObserver extends BaseObserver implements edu.byu.cs.tweeter.client.model.service.FollowObserver {

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

        @Override
        public String getTaskName() {
            return "update follower/following count";   // fixme
        }

        @Override
        protected void extra() {
            // none
        }
    }

    private class LogoutObserver extends BaseObserver implements SimpleObserver {

        @Override
        public void handleSuccess() {
            view.logOut();
        }

        @Override
        public String getTaskName() {
            return "logout";
        }

        @Override
        protected void extra() {
            // none
        }
    }

    private class StatusObserver extends BaseObserver implements SimpleObserver {

        @Override
        public void handleSuccess() {
            view.postStatus();
        }

        @Override
        public String getTaskName() {
            return "post status";
        }

        @Override
        protected void extra() {
            // none
        }
    }
}
