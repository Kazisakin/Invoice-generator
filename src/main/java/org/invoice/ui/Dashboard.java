package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.invoice.controller.InvoiceController;
import org.invoice.controller.LoginController;
import org.invoice.domain.User;
import org.invoice.repository.UserRepositoryImpl;
import org.invoice.service.UserService;

public class Dashboard {
    private final InvoiceController invoiceController;
    private User currentUser;
    private HBox navigationBar;

    public Dashboard(InvoiceController ic, User user) {
        this.invoiceController = ic;
        this.currentUser = user;
        initNavigationBar();
    }

    public void setUser(User u) {
        this.currentUser = u;
    }

    public HBox getNavigationBar() {
        return navigationBar;
    }

    private void initNavigationBar() {
        navigationBar = new HBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setAlignment(Pos.CENTER_LEFT);
        navigationBar.setStyle("-fx-background-color: #2C3E50;");

        Button createInvoiceBtn = new Button("Create Invoice");
        Button listInvoicesBtn = new Button("List Invoices");
        Button generatePdfBtn = new Button("Generate PDF");
        Button logoutBtn = new Button("Logout");

        String style = "-fx-background-color: #34495E; -fx-text-fill: white;";
        createInvoiceBtn.setStyle(style);
        listInvoicesBtn.setStyle(style);
        generatePdfBtn.setStyle(style);
        logoutBtn.setStyle(style);

        createInvoiceBtn.setOnAction(e -> {
            InvoiceForm form = new InvoiceForm(invoiceController);
            ((BorderPane) navigationBar.getParent()).setCenter(form);
        });

        listInvoicesBtn.setOnAction(e -> {
            InvoiceListPanel listPanel = new InvoiceListPanel(invoiceController);
            ((BorderPane) navigationBar.getParent()).setCenter(listPanel);
        });

        generatePdfBtn.setOnAction(e -> {
            PdfPanel pdf = new PdfPanel(invoiceController);
            ((BorderPane) navigationBar.getParent()).setCenter(pdf);
        });

        logoutBtn.setOnAction(e -> {
            BorderPane bp = (BorderPane) navigationBar.getParent();
            bp.setTop(null);
            LoginScreen loginScreen = new LoginScreen(
                    new LoginController(
                            new UserService(new UserRepositoryImpl())
                    ),
                    user -> {
                        Dashboard newDashboard = new Dashboard(invoiceController, user);
                        bp.setTop(newDashboard.getNavigationBar());
                        // Possibly show some other panel as default
                    }
            );
            bp.setCenter(loginScreen);
        });

        navigationBar.getChildren().addAll(createInvoiceBtn, listInvoicesBtn, generatePdfBtn, logoutBtn);
    }
}
