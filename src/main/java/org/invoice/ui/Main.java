package org.invoice.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.invoice.controller.InvoiceController;
import org.invoice.controller.LoginController;
import org.invoice.domain.User;
import org.invoice.repository.*;
import org.invoice.service.InvoiceService;
import org.invoice.service.UserService;

/**
 * Main JavaFX entry point.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Setup repositories
        InvoiceRepository invoiceRepo = new InvoiceRepositoryImpl();
        UserRepository userRepo = new UserRepositoryImpl();
        // Add CourseRepository, StudentRepository, etc. if needed

        // Setup services
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);
        UserService userService = new UserService(userRepo);

        // Setup controllers
        InvoiceController invoiceController = new InvoiceController(invoiceService);
        LoginController loginController = new LoginController(userService);

        // Create the LoginScreen
        LoginScreen loginScreen = new LoginScreen(loginController);
        // Provide a callback for when login succeeds
        loginScreen.setOnLoginSuccess((User user) -> {
            // user is the successfully logged-in User
            MainView mainView = new MainView(invoiceController, user);
            Scene mainScene = new Scene(mainView, 1000, 700);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Invoice Management - " + user.getUsername());
        });

        // Show the login screen first
        Scene loginScene = new Scene(loginScreen, 600, 400);
        primaryStage.setTitle("Invoice Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
