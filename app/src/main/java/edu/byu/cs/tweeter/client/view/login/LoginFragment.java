package edu.byu.cs.tweeter.client.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the login screen.
 */
public class LoginFragment extends Fragment implements LoginPresenter.View {
    private static final String LOG_TAG = "LoginFragment";

    private Toast loginToast;
    private EditText alias;
    private EditText password;
    private TextView errorView;
    private LoginPresenter presenter = new LoginPresenter(this);

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        alias = view.findViewById(R.id.loginUsername);
        password = view.findViewById(R.id.loginPassword);
        errorView = view.findViewById(R.id.loginError);
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                errorView.setText(null);
                presenter.initiateLogin(alias.getText().toString(), password.getText().toString());

            }
        });

        return view;
    }

    @Override
    public void displayInfoMessage(String message) {
        loginToast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        loginToast.show();
    }

    @Override
    public void displayErrorMessage(String message) {
        errorView.setText(message);
    }

    @Override
    public void loginSuccessful(User user, AuthToken authToken) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);

        loginToast.cancel();

        Toast.makeText(getContext(), "Hello " + Cache.getInstance().getCurrUser().getName(),
                Toast.LENGTH_LONG).show();
        System.out.println("Hello " + Cache.getInstance().getCurrUser().getName());
        startActivity(intent);
    }

    @Override
    public void displayMessage(String message) {
        // fixme?
    }
}
