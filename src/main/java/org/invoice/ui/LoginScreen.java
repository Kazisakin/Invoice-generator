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
    private final Consumer<User> onLoginSuccess;

    public LoginScreen(LoginController ctrl, Consumer<User> successCallback){
        loginController = ctrl;
        onLoginSuccess = successCallback;

        setSpacing(20);
        setPadding(new Insets(25));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to right, #34495E, #2C3E50);");

        Label title=new Label("Login to Invoice System");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #ECF0F1; -fx-font-weight: bold;");

        TextField userField=new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(220);

        PasswordField passField=new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(220);

        Button loginBtn=new Button("Login");
        loginBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        loginBtn.setPrefWidth(100);

        Label feedback=new Label();
        feedback.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        loginBtn.setOnAction(e->{
            String username = userField.getText().trim();
            String password = passField.getText().trim();
            if(username.isEmpty()||password.isEmpty()){
                feedback.setText("Please fill both fields");
                return;
            }
            User user = loginController.login(username, password);
            if(user!=null){
                feedback.setText("Login success");
                onLoginSuccess.accept(user);
            } else {
                feedback.setText("Invalid credentials");
            }
        });

        getChildren().addAll(title, userField, passField, loginBtn, feedback);
    }
}
