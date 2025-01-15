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

public class Main extends Application {
    private Stage primaryStage;

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
        launch(args);
    }
}
