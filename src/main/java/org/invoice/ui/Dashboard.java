package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import org.invoice.controller.LoginController;
import org.invoice.domain.User;
import org.invoice.repository.UserRepositoryImpl;
import org.invoice.service.UserService;

public class Dashboard {

    private User currentUser;
    private HBox navigationBar;

    public Dashboard() {
        initNavigationBar();
    }

    public void setUser(User user) {
        this.currentUser = user;
        // You can use this user information as needed, e.g., display username
    }

    public HBox getNavigationBar() {
        return navigationBar;
    }

    private void initNavigationBar() {
        navigationBar = new HBox();
        navigationBar.setSpacing(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setAlignment(Pos.CENTER_LEFT);
        navigationBar.setStyle("-fx-background-color: #2C3E50;");

        Button createInvoiceBtn = new Button("Create Invoice");
        Button listInvoicesBtn = new Button("List Invoices");
        Button generatePdfBtn = new Button("Generate PDF");
        Button logoutBtn = new Button("Logout");

        // Style the buttons
        String buttonStyle = "-fx-background-color: #34495E; -fx-text-fill: white;";
        createInvoiceBtn.setStyle(buttonStyle);
        listInvoicesBtn.setStyle(buttonStyle);
        generatePdfBtn.setStyle(buttonStyle);
        logoutBtn.setStyle(buttonStyle);

        // Set button widths for consistency
        createInvoiceBtn.setPrefWidth(120);
        listInvoicesBtn.setPrefWidth(120);
        generatePdfBtn.setPrefWidth(120);
        logoutBtn.setPrefWidth(80);

        // Handle button actions
        createInvoiceBtn.setOnAction(e -> {
            // Switch to Invoice Form
            InvoiceForm invoiceForm = new InvoiceForm();
            ((BorderPane) navigationBar.getParent()).setCenter(invoiceForm);
        });

        listInvoicesBtn.setOnAction(e -> {
            // Switch to Invoice List
            InvoiceListPanel invoiceList = new InvoiceListPanel();
            ((BorderPane) navigationBar.getParent()).setCenter(invoiceList);
        });

        generatePdfBtn.setOnAction(e -> {
            // Switch to PDF Generation Panel
            PdfPanel pdfPanel = new PdfPanel();
            ((BorderPane) navigationBar.getParent()).setCenter(pdfPanel);
        });

        logoutBtn.setOnAction(e -> {
            // Return to Login Screen
            BorderPane mainLayout = (BorderPane) navigationBar.getParent();
            mainLayout.setTop(null);
            mainLayout.setCenter(new LoginScreen(new LoginController(new UserService(new UserRepositoryImpl()))));
        });

        navigationBar.getChildren().addAll(createInvoiceBtn, listInvoicesBtn, generatePdfBtn, logoutBtn);
    }
}
