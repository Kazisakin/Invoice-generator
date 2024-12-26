package org.invoice.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.invoice.controller.LoginController;
import org.invoice.domain.User;
import org.invoice.repository.UserRepository;
import org.invoice.repository.UserRepositoryImpl;
import org.invoice.service.UserService;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize repositories and services
        UserRepository userRepo = new UserRepositoryImpl();
        UserService userService = new UserService(userRepo);
        LoginController loginController = new LoginController(userService);

        // Create the main layout
        BorderPane mainLayout = new BorderPane();

        // Initialize different UI panels
        LoginScreen loginScreen = new LoginScreen(loginController);
        Dashboard dashboard = new Dashboard();

        // Set the initial center to the login screen
        mainLayout.setCenter(loginScreen);

        // Handle successful login to switch to the dashboard
        loginScreen.setOnLoginSuccess(user -> {
            // Pass the user to the dashboard if needed
            dashboard.setUser(user);
            mainLayout.setTop(dashboard.getNavigationBar());
            mainLayout.setCenter(new WelcomePanel()); // Display WelcomePanel after login
        });

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 800, 600);
        // If using CSS
        primaryStage.setTitle("JavaFX Invoice Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
