package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    private static Fragment fragment;

    public interface LoginObserver {
        void handleSuccess(User user, AuthToken authToken);
        void handleFailure(String message); // to explain failure
        void handleException(Exception exception);

    }

    public void login(String alias, String password, LoginObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);

    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    public static class GetUserHandler extends Handler {

        public GetUserHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);

                Intent intent = new Intent(fragment.getContext(), MainActivity.class);
                intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
                fragment.startActivity(intent);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                Toast.makeText(fragment.getContext(), "Failed to get user's profile: " + message, Toast.LENGTH_LONG).show();
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                Toast.makeText(fragment.getContext(), "Failed to get user's profile because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
