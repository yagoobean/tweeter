package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {
    public static final String AUTH_TOKEN_KEY = "auth-token";
    public static final String USER_KEY = "user";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;

    private User authenticatedUser;
    private AuthToken authToken;

    public AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    protected Pair<User, AuthToken> authenticate() {
        User authenticatedUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(authenticatedUser, authToken);
    }

    // this seems redundant, when I could fill the data members in authenticate()
    @Override
    protected void processTask() {
        Pair<User, AuthToken> authResult = authenticate();
        authenticatedUser = authResult.getFirst();
        authToken = authResult.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
