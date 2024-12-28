package org.invoice.ui;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;
import org.invoice.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

public class PdfPanel extends VBox {

    private static final Logger logger = LoggerFactory.getLogger(PdfPanel.class);
    private final InvoiceController invoiceController;

    public PdfPanel(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
        initUI();
    }

    private void initUI() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Generate Invoice PDF");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField invoiceIdField = new TextField();
        invoiceIdField.setPromptText("Enter Invoice ID");

        Button generateBtn = new Button("Generate PDF");
        Label feedback = new Label();

        generateBtn.setOnAction(e -> {
            String idTxt = invoiceIdField.getText().trim();
            if (idTxt.isEmpty()) {
                feedback.setText("Please enter an invoice ID.");
                feedback.setStyle("-fx-text-fill: red;");
                return;
            }
            int invoiceId;
            try {
                invoiceId = Integer.parseInt(idTxt);
            } catch (NumberFormatException ex) {
                feedback.setText("Invoice ID must be numeric!");
                feedback.setStyle("-fx-text-fill: red;");
                return;
            }

            try {
                Invoice invoice = invoiceController.findInvoiceById(invoiceId);
                if (invoice == null) {
                    feedback.setText("Invoice not found.");
                    feedback.setStyle("-fx-text-fill: red;");
                    return;
                }
                // Prompt user for save location
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save PDF");
                fileChooser.setInitialFileName("Invoice_" + invoiceId + ".pdf");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                java.io.File file = fileChooser.showSaveDialog(this.getScene().getWindow());
                if (file == null) return; // user canceled

                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document doc = new Document(pdfDoc);

                doc.add(new Paragraph("Invoice ID: " + invoice.getId()));
                doc.add(new Paragraph("Student Name: " + invoice.getStudent().getName()));
                doc.add(new Paragraph("Course: " + invoice.getCourse().getName()));
                doc.add(new Paragraph("Fee: " + invoice.getCourse().getFee()));
                doc.add(new Paragraph("Discount: " + invoice.getDiscount()));
                doc.add(new Paragraph("Total Amount: " + invoice.getTotalAmount()));
                doc.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));

                doc.close();
                feedback.setText("PDF saved to: " + file.getAbsolutePath());
                feedback.setStyle("-fx-text-fill: green;");
                logger.info("PDF generated for invoice ID: {}", invoiceId);
            } catch (ServiceException ex) {
                feedback.setText("Error retrieving invoice: " + ex.getMessage());
                feedback.setStyle("-fx-text-fill: red;");
                logger.error("Failed to retrieve invoice ID: {}", idTxt, ex);
            } catch (FileNotFoundException ex) {
                feedback.setText("File creation error: " + ex.getMessage());
                feedback.setStyle("-fx-text-fill: red;");
                logger.error("File error", ex);
            }
        });

        getChildren().addAll(title, invoiceIdField, generateBtn, feedback);
    }
}
