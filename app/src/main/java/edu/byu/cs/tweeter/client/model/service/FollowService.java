package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface Observer {

        void displayError(String message);

        void displayException(Exception ex, String header);

        void addItems(List<User> items, boolean hasMorePages);

        // maybe move this? Make a separate observer? Idk
        void updateFollowersCount(int count);
        void updateFollowingCount(int count);
        void setFollowers(boolean isFollower);
        void updateFollowButton(boolean val);

    }

    public void loadMoreFollowees(User user, int pageSize, User lastFollowee, Observer observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollowee, Observer observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, Observer observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }

    public void executeIsFollowerTask(User selectedUser, Observer observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void executeFollowTask(User selectedUser, Observer observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer, selectedUser));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void executeUnfollowTask(User selectedUser, Observer observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer, selectedUser));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    // FollowHandler

    private class FollowHandler extends Handler {
        private User selectedUser;
        private Observer observer;

        public FollowHandler(Observer observer, User selectedUser) {
            super(Looper.getMainLooper());
            this.observer = observer;
            this.selectedUser = selectedUser;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                updateSelectedUserFollowingAndFollowers(selectedUser, observer);
                // updateFollowButton(false);
                observer.updateFollowButton(false);
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                // Toast.makeText(MainActivity.this, "Failed to follow: " + message, Toast.LENGTH_LONG).show();
                observer.displayError("Failed to follow: " + message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                // Toast.makeText(MainActivity.this, "Failed to follow because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.displayException(ex, "Failed to follow because of exception: ");
            }

            // followButton.setEnabled(true);
            observer.updateFollowButton(true);
        }
    }

    private class GetFollowingHandler extends Handler {

        private Observer observer;

        public GetFollowingHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);

                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.addItems(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.displayError("Failed to get following: " + message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.displayException(ex, "Failed to get following because of exception: ");
            }
        }
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handler {
        private Observer observer;

        public GetFollowingCountHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                // followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
                observer.updateFollowingCount(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                // Toast.makeText(MainActivity.this, "Failed to get following count: " + message, Toast.LENGTH_LONG).show();
                observer.displayError("Failed to get following count: " + message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                // Toast.makeText(MainActivity.this, "Failed to get following count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.displayException(ex, "Failed to get following because of exception: ");
            }
        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {
        private Observer observer;

        public GetFollowersHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);

                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.addItems(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.displayError("Failed to get followers: " + message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.displayException(ex, "Failed to get followers because of exception: ");
            }
        }
    }

    private class GetFollowersCountHandler extends Handler {
        private Observer observer;

        public GetFollowersCountHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                // followerCount.setText(getString(R.string.followerCount, String.valueOf(count)));
                observer.updateFollowersCount(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                // Toast.makeText(MainActivity.this, "Failed to get followers count: " + message, Toast.LENGTH_LONG).show();
                observer.displayError("Failed to get followers count: " + message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                // Toast.makeText(MainActivity.this, "Failed to get followers count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.displayException(ex, "Failed to get followers count because of exception: ");
            }
        }
    }

    // IsFollowerHandler

    private class IsFollowerHandler extends Handler {
        private Observer observer;

        public IsFollowerHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
//                // If logged in user if a follower of the selected user, display the follow button as "following"
//                if (isFollower) {
//                    followButton.setText(R.string.following);
//                    followButton.setBackgroundColor(getResources().getColor(R.color.white));
//                    followButton.setTextColor(getResources().getColor(R.color.lightGray));
//                } else {
//                    followButton.setText(R.string.follow);
//                    followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                }
                observer.setFollowers(isFollower);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                // Toast.makeText(MainActivity.this, "Failed to determine following relationship: " + message, Toast.LENGTH_LONG).show();
                observer.displayError("Failed to determine following relationship: " + message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                // Toast.makeText(MainActivity.this, "Failed to determine following relationship because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.displayException(ex, "Failed to determine following relationship because of exception: ");
            }
        }
    }

    // UnfollowHandler

    private class UnfollowHandler extends Handler {
        private Observer observer;
        private User selectedUser;

        public UnfollowHandler(Observer observer, User selectedUser) {
            super(Looper.getMainLooper());
            this.observer = observer;
            this.selectedUser = selectedUser;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                updateSelectedUserFollowingAndFollowers(selectedUser, observer);
                observer.updateFollowButton(true);
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                // Toast.makeText(MainActivity.this, "Failed to unfollow: " + message, Toast.LENGTH_LONG).show();
                observer.displayError("Failed to unfollow: " + message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                // Toast.makeText(MainActivity.this, "Failed to unfollow because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.displayException(ex, "Failed to unfollow because of exception: ");
            }

            // followButton.setEnabled(true);
            observer.updateFollowButton(true);
        }
    }
}
