package edu.byu.cs.tweeter.client.model.service;

public interface FollowObserver extends BackgroundObserver{
    // maybe move this? Make a separate observer? Idk
    void updateFollowersCount(int count);

    void updateFollowingCount(int count);

    void setFollowers(boolean isFollower);

    void updateFollowButton(boolean val);
}
