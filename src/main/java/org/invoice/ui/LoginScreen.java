package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.invoice.controller.LoginController;
import org.invoice.domain.User;

import java.util.function.Consumer;

public class LoginScreen extends VBox {

    private final LoginController loginController;
    private Consumer<User> onLoginSuccess;

    public LoginScreen(LoginController loginController) {
        this.loginController = loginController;
        initUI();
    }

    private void initUI() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        Button loginBtn = new Button("Login");
        loginBtn.setDefaultButton(true);

        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                feedbackLabel.setText("Please enter both username and password.");
                return;
            }

            User user = loginController.login(username, password);
            if (user != null) {
                feedbackLabel.setStyle("-fx-text-fill: green;");
                feedbackLabel.setText("Login successful! Welcome " + user.getUsername());
                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(user);
                }
            } else {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Invalid username or password.");
            }
        });

        getChildren().addAll(titleLabel, usernameField, passwordField, loginBtn, feedbackLabel);
    }

    public void setOnLoginSuccess(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
}
