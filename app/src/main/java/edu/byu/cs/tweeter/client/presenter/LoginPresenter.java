package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements UserService.LoginObserver {
    private View view;

    public LoginPresenter(View view) {
        this.view = view;
    }

    @Override
    public void handleSuccess(User user, AuthToken authToken) {
        view.loginSuccessful(user, authToken);
    }

    @Override
    public void handleFailure(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void handleException(Exception exception) {
        view.displayInfoMessage(exception.getMessage());   // TODO
    }

    public interface View {
        public void displayInfoMessage(String message);
        public void displayErrorMessage(String message);
        public void loginSuccessful(User user, AuthToken authToken);
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
}
