package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends BackgroundPresenter<LoginPresenter.View> implements AuthenticatedObserver {

    public interface View extends BackgroundView {

        void displayInfoMessage(String message);
        void displayErrorMessage(String message);
        void loginSuccessful(User user, AuthToken authToken);
    }

    // private final View view;

    public LoginPresenter(View view) {
        super(view);
    }

    public void initiateLogin(String alias, String password) {
        String validationMessage = validateLogin(alias, password);

        if (validationMessage == null) {
            view.displayInfoMessage("Logging in ....");
            UserService service = new UserService();
            service.login(alias, password, this);
        }
        else {
            view.displayErrorMessage(validationMessage);
        }

    }

    public String validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }

    @Override
    public void postStatus(User user, AuthToken authToken) {
        view.loginSuccessful(user, authToken);
    }

    @Override
    public void displayError(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void displayException(Exception exception) {
        view.displayInfoMessage("Failed to login because of exception: " + exception.getMessage());
    }

    // not needed here...
    @Override
    public String getTaskName() {
        return "login";
    }
}
