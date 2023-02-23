package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.ItemObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter extends PagedPresenter<User> {

    public interface View extends PagedView<User> {
        void addMoreItems(List<User> followers);
        void getUser(User user);
    }

    private FollowService followService;

    public GetFollowersPresenter(View view) {
        super(view);
        this.followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreFollowers(user, PAGE_SIZE, lastItem, new GetFollowersObserver());
        }
    }

    private class GetFollowersObserver extends PagedObserver {
        @Override
        public String getTaskName() {
            return "get followers";
        }

        @Override
        protected void extra() {
            isLoading = false;
            view.setLoadingFooter(false);
        }
    }

}
