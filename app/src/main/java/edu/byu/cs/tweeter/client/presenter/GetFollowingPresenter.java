package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.ItemObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends PagedPresenter<User> {

    public interface View extends PagedView<User> {
        void addMoreItems(List<User> followees);
        void getUser(User user);
    }

    private FollowService followService;

    public GetFollowingPresenter(View view) {
        super(view);
        this.followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreFollowees(user, PAGE_SIZE, lastItem, new GetFollowingObserver());
        }
    }

    public class GetFollowingObserver extends PagedObserver {
        @Override
        public String getTaskName() {
            return "get following";
        }

        @Override
        protected void extra() {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }
    }
}
