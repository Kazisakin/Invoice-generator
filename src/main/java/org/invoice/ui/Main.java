package org.invoice.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.invoice.controller.InvoiceController;
import org.invoice.controller.LoginController;
import org.invoice.repository.InvoiceRepositoryImpl;
import org.invoice.repository.UserRepositoryImpl;
import org.invoice.service.InvoiceService;
import org.invoice.service.UserService;
import org.invoice.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    private Stage primaryStage;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage){
        primaryStage = stage;

        InvoiceController invCtrl = new InvoiceController(new InvoiceService(new InvoiceRepositoryImpl()));
        LoginController loginCtrl = new LoginController(new UserService(new UserRepositoryImpl()));

        LoginScreen loginScreen = new LoginScreen(loginCtrl, user->{
            MainView mv = new MainView(invCtrl, user, primaryStage);
            Scene mainScene = new Scene(mv, 1000, 700);
            stage.setScene(mainScene);
            stage.setTitle("Invoice System - " + user.getUsername());
        });
        Scene loginScene = new Scene(loginScreen, 600, 400);
        stage.setScene(loginScene);
        stage.setTitle("Login - Invoice System");
        stage.show();
    }

    public static void main(String[] args){
        logger.info("SLF4J and Logback are working!");
        Application.launch(Main.class, args);
    }
}
