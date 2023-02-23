package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.ItemObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends BackgroundPresenter<PagedPresenter.PagedView<T>> {
    public PagedPresenter(PagedView<T> view) {
        super(view);
        this.userService = new UserService();
    }

    // User targetUser;
    // AuthToken authToken;
    // boolean isGettingUser;
    public interface PagedView<U> extends BackgroundView {

        void setLoadingFooter(boolean isLoading);
        void addMoreItems(List<U> items);
        void getUser(User user);
    }

    protected UserService userService;
    protected static final int PAGE_SIZE = 10;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    // protected abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);
    // protected abstract String getDescription();

    // public void loadMoreItems();
    // public void getUser(String alias);


    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public abstract class PagedObserver implements ItemObserver<T> {
        protected abstract String getTaskName();
        protected abstract void extra();

        @Override
        public void displayError(String message) {
            extra();
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception exception) {
            extra();
            view.displayMessage("Failed to " + getTaskName() + " because of exception: " + exception.getMessage());
        }

        @Override
        public void addItems(List<T> items, boolean hasMorePages) {
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);

            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.addMoreItems(items);
        }

        public void postStatus(User user, AuthToken authToken) {
            view.getUser(user);
        }
    }

    public class GetUserObserver extends PagedObserver {
        @Override
        protected String getTaskName() {
            return "get user's profile";
        }

        @Override
        protected void extra() {
            // do nothing
        }
    }
}
