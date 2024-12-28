package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import org.invoice.controller.InvoiceController;
import org.invoice.controller.LoginController;
import org.invoice.domain.User;
import org.invoice.repository.UserRepositoryImpl;
import org.invoice.service.UserService;

public class Dashboard {

    private final InvoiceController invoiceController;
    private User currentUser;
    private HBox navigationBar;

    /**
     * Constructs a Dashboard with the specified InvoiceController.
     */
    public Dashboard(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
        initNavigationBar();
    }

    public void setUser(User user) {
        this.currentUser = user;
        // You can use this user information as needed, e.g., display the username
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
            // We pass the invoiceController to InvoiceForm
            InvoiceForm invoiceForm = new InvoiceForm(invoiceController);
            ((BorderPane) navigationBar.getParent()).setCenter(invoiceForm);
        });

        listInvoicesBtn.setOnAction(e -> {
            InvoiceListPanel invoiceList = new InvoiceListPanel(invoiceController);
            ((BorderPane) navigationBar.getParent()).setCenter(invoiceList);
        });

        generatePdfBtn.setOnAction(e -> {
            PdfPanel pdfPanel = new PdfPanel(invoiceController);
            ((BorderPane) navigationBar.getParent()).setCenter(pdfPanel);
        });

        logoutBtn.setOnAction(e -> {
            // Return to Login Screen
            BorderPane mainLayout = (BorderPane) navigationBar.getParent();
            mainLayout.setTop(null);

            LoginScreen loginScreen = new LoginScreen(
                    new LoginController(new UserService(new UserRepositoryImpl()))
            );

            // If you want a callback for re-login, do this:
            loginScreen.setOnLoginSuccess(loggedInUser -> {
                // Possibly reconstruct Dashboard or MainView, etc.
            });

            mainLayout.setCenter(loginScreen);
        });

        navigationBar.getChildren().addAll(createInvoiceBtn, listInvoicesBtn, generatePdfBtn, logoutBtn);
    }
}
