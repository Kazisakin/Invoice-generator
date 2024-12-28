package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A unified main view with a navigation bar (top),
 * and dynamic center content.
 */
public class MainView extends BorderPane {

    private static final Logger logger = LoggerFactory.getLogger(MainView.class);

    private final InvoiceController invoiceController;
    private final User currentUser;

    /**
     * Constructs a MainView with the specified InvoiceController and logged-in User.
     *
     * @param invoiceController the controller for invoice operations
     * @param user the logged-in User
     */
    public MainView(InvoiceController invoiceController, User user) {
        this.invoiceController = invoiceController;
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        // Top navigation
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(10));
        navBar.setStyle("-fx-background-color: #2C3E50;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        // Navigation Buttons
        Button homeBtn = new Button("Home");
        styleNavButton(homeBtn);

        Button createInvoiceBtn = new Button("Create Invoice");
        styleNavButton(createInvoiceBtn);

        Button listInvoicesBtn = new Button("List Invoices");
        styleNavButton(listInvoicesBtn);

        Button generatePdfBtn = new Button("Generate PDF");
        styleNavButton(generatePdfBtn);

        Button logoutBtn = new Button("Logout");
        styleNavButton(logoutBtn);

        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 0 0 0 20;");

        navBar.getChildren().addAll(homeBtn, createInvoiceBtn, listInvoicesBtn, generatePdfBtn, logoutBtn, welcomeLabel);
        setTop(navBar);

        // Default center content (Home)
        setCenter(new WelcomePanel());

        // Button actions
        homeBtn.setOnAction(e -> setCenter(new WelcomePanel()));

        createInvoiceBtn.setOnAction(e -> {
            InvoiceForm form = new InvoiceForm(invoiceController);
            setCenter(form);
        });

        listInvoicesBtn.setOnAction(e -> {
            InvoiceListPanel listPanel = new InvoiceListPanel(invoiceController);
            setCenter(listPanel);
        });

        generatePdfBtn.setOnAction(e -> {
            PdfPanel pdfPanel = new PdfPanel(invoiceController);
            setCenter(pdfPanel);
        });

        logoutBtn.setOnAction(e -> {
            // Build a new LoginScreen (fresh login)
            LoginScreen newLoginScreen = new LoginScreen(
                    // create a new LoginController if needed
                    new org.invoice.controller.LoginController(
                            new org.invoice.service.UserService(new org.invoice.repository.UserRepositoryImpl())
                    )
            );
            // On login success, show a new MainView
            newLoginScreen.setOnLoginSuccess(loggedInUser -> {
                MainView newMainView = new MainView(invoiceController, loggedInUser);
                getScene().setRoot(newMainView);
            });
            getScene().setRoot(newLoginScreen);
            logger.info("User {} logged out.", currentUser.getUsername());
        });
    }

    private void styleNavButton(Button btn) {
        btn.setStyle("-fx-background-color: #34495E; -fx-text-fill: white;");
        btn.setPrefHeight(30);
    }
}
