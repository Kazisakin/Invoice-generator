package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.User;

public class MainView extends BorderPane {
    private final InvoiceController invoiceController;
    private final User currentUser;
    private final Stage primaryStage;

    public MainView(InvoiceController ic, User u, Stage st){
        invoiceController=ic;
        currentUser=u;
        primaryStage=st;
        initUI();
    }

    private void initUI(){
        setStyle("-fx-background-color: #ECF0F1;");

        HBox nav=new HBox(20);
        nav.setPadding(new Insets(15));
        nav.setStyle("-fx-background-color: #2C3E50;");
        nav.setAlignment(Pos.CENTER_LEFT);

        Button homeBtn=new Button("Home");
        styleNavButton(homeBtn);

        Button createInvoiceBtn=new Button("Create Invoice");
        styleNavButton(createInvoiceBtn);

        Button listInvoiceBtn=new Button("List Invoices");
        styleNavButton(listInvoiceBtn);

        Button logoutBtn=new Button("Logout");
        styleNavButton(logoutBtn);

        Label welcome=new Label("Hi, " + currentUser.getUsername());
        welcome.setStyle("-fx-text-fill: white; -fx-font-weight:bold;");

        nav.getChildren().addAll(homeBtn, createInvoiceBtn, listInvoiceBtn, logoutBtn, welcome);
        setTop(nav);

        homeBtn.setOnAction(e->setCenter(new WelcomePanel()));
        createInvoiceBtn.setOnAction(e->setCenter(new InvoiceForm(invoiceController)));
        listInvoiceBtn.setOnAction(e->setCenter(new InvoiceListPanel(invoiceController)));
        logoutBtn.setOnAction(e->{
            LoginScreen ls=new LoginScreen(
                    new org.invoice.controller.LoginController(
                            new org.invoice.service.UserService(new org.invoice.repository.UserRepositoryImpl())
                    ),
                    user->{
                        MainView mv=new MainView(invoiceController, user, primaryStage);
                        Scene mainScene=new Scene(mv,1000,700);
                        primaryStage.setScene(mainScene);
                        primaryStage.setTitle("Invoice System - " + user.getUsername());
                    }
            );
            Scene loginScene=new Scene(ls,600,400);
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login - Invoice System");
        });
        setCenter(new WelcomePanel());
    }

    private void styleNavButton(Button btn){
        btn.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-font-weight: bold;");
        btn.setPrefHeight(34);
    }
}
