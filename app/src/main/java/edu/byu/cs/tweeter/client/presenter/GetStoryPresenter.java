package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
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

    public class GetStoryObserver extends PagedObserver {

        @Override
        public String getTaskName() {
            return "get story";
        }

        @Override
        protected void extra() {
            isLoading = false;
            view.setLoadingFooter(isLoading);

        }

    }
}
