package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements AuthenticatedObserver {

    public interface View {

        public void displayInfoMessage(String message);

        public void displayErrorMessage(String message);
        public void registerSuccessful(User user, AuthToken authToken);
    }
    private View view;

    public RegisterPresenter(View view) {
        this.view = view;
    }

    public void initiateRegister(String firstName, String lastName, String alias, String password, Drawable imageToUpload) {
        String validationMessage = validateRegistration(firstName, lastName, alias, password, imageToUpload);

        if (validationMessage == null) {  // it was successful
            view.displayInfoMessage("Registering...");

            // Convert image to byte array.
            Bitmap image = ((BitmapDrawable) imageToUpload).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            UserService service = new UserService();
            service.register(firstName, lastName, alias, password, imageBytesBase64, this);

        }
        else {
            view.displayErrorMessage(validationMessage);
        }

    }

    public String validateRegistration(String firstName, String lastName, String alias, String password, Drawable imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
        return null;
    }

    @Override
    public void postStatus(User user, AuthToken authToken) {
        view.registerSuccessful(user, authToken);
    }

    @Override
    public void displayError(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void displayException(Exception exception) {
        view.displayInfoMessage("Failed to register because of exception: " + exception.getMessage());
    }
}
