package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.invoice.domain.User;

public class Dashboard extends VBox {

    private User currentUser;

    public Dashboard() {
        initUI();
    }

    public void setUser(User user) {
        this.currentUser = user;
        // You can use this user information as needed
    }

    private void initUI() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Button createInvoiceBtn = new Button("Create Invoice");
        createInvoiceBtn.setMaxWidth(200);

        Button listInvoicesBtn = new Button("List Invoices");
        listInvoicesBtn.setMaxWidth(200);

        Button generatePdfBtn = new Button("Generate PDF");
        generatePdfBtn.setMaxWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setMaxWidth(200);

        // Handle button actions
        createInvoiceBtn.setOnAction(e -> {
            // Switch to Invoice Form
            getScene().lookup(".border-pane").setUserData("invoiceForm");
            // Alternatively, use a central controller to switch views
            // For simplicity, you can embed the InvoiceForm here
            InvoiceForm invoiceForm = new InvoiceForm();
            ((BorderPane) getParent()).setCenter(invoiceForm);
        });

        listInvoicesBtn.setOnAction(e -> {
            // Switch to Invoice List
            InvoiceListPanel invoiceList = new InvoiceListPanel();
            ((BorderPane) getParent()).setCenter(invoiceList);
        });

        generatePdfBtn.setOnAction(e -> {
            // Switch to PDF Generation Panel
            PdfPanel pdfPanel = new PdfPanel();
            ((BorderPane) getParent()).setCenter(pdfPanel);
        });

        logoutBtn.setOnAction(e -> {
            // Return to Login Screen
            LoginScreen loginScreen = new LoginScreen(null); // Pass appropriate controller if needed
            ((BorderPane) getParent()).setCenter(loginScreen);
        });

        getChildren().addAll(createInvoiceBtn, listInvoicesBtn, generatePdfBtn, logoutBtn);
    }
}
