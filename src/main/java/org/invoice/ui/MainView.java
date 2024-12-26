package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MainView extends BorderPane {

    private HBox topMenu;     // Our nav bar
    private InvoiceForm invoiceForm;
    private InvoiceListPanel invoiceListPanel;
    private PdfPanel pdfPanel; // Hypothetical panel for PDF generation

    public MainView() {
        initUI();
    }

    private void initUI() {
        // Create the top menu bar
        topMenu = new HBox();
        topMenu.setSpacing(10);
        topMenu.setPadding(new Insets(10));

        Button createInvoiceBtn = new Button("Create Invoice");
        Button listInvoicesBtn = new Button("List Invoices");
        Button generatePdfBtn  = new Button("Generate PDF");

        // Optional: a "Back" or "Home" button if you want
        Button homeBtn = new Button("Home");

        // Initialize sub-panels (center content)
        invoiceForm = new InvoiceForm();
        invoiceListPanel = new InvoiceListPanel();
        pdfPanel = new PdfPanel();

        // Button events
        createInvoiceBtn.setOnAction(e -> setCenter(invoiceForm));
        listInvoicesBtn.setOnAction(e -> setCenter(invoiceListPanel));
        generatePdfBtn.setOnAction(e -> setCenter(pdfPanel));
        homeBtn.setOnAction(e -> setCenter(null)); // Clears or shows a home screen

        topMenu.getChildren().addAll(homeBtn, createInvoiceBtn, listInvoicesBtn, generatePdfBtn);

        // Put topMenu at the top of this BorderPane
        setTop(topMenu);

        // Optionally, show a default center panel or a "Welcome" message
        // setCenter(new WelcomePanel()); // or null
    }
}
