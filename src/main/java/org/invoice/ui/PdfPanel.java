package org.invoice.ui;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;
import org.invoice.repository.InvoiceRepository;
import org.invoice.repository.InvoiceRepositoryImpl;
import org.invoice.service.InvoiceService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class PdfPanel extends VBox {

    private final InvoiceController invoiceController;
    private ComboBox<Integer> invoiceIdComboBox;
    private Label feedbackLabel;

    public PdfPanel() {
        // Initialize repository and service
        InvoiceRepository invoiceRepo = new InvoiceRepositoryImpl();
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);
        this.invoiceController = new InvoiceController(invoiceService);

        initUI();
    }

    private void initUI() {
        setSpacing(20);
        setPadding(new Insets(20));

        Label title = new Label("Generate PDF");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label selectInvoiceLabel = new Label("Select Invoice ID:");
        invoiceIdComboBox = new ComboBox<>();
        loadInvoiceIds();

        Button generateBtn = new Button("Export Selected Invoice to PDF");
        generateBtn.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white;");
        generateBtn.setPrefWidth(200);

        feedbackLabel = new Label();

        generateBtn.setOnAction(e -> {
            Integer selectedId = invoiceIdComboBox.getValue();
            if (selectedId == null) {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Please select an Invoice ID.");
                return;
            }

            Invoice invoice = invoiceController.findInvoiceById(selectedId);
            if (invoice == null) {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Invoice not found.");
                return;
            }

            // Open FileChooser to select save location
            Stage stage = (Stage) this.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Invoice PDF");
            fileChooser.setInitialFileName("invoice_" + selectedId + ".pdf");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );

            java.io.File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    createPdf(file.getAbsolutePath(), invoice);
                    feedbackLabel.setStyle("-fx-text-fill: green;");
                    feedbackLabel.setText("PDF generated successfully at " + file.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                    feedbackLabel.setText("Error generating PDF: " + ex.getMessage());
                }
            }
        });

        getChildren().addAll(title, selectInvoiceLabel, invoiceIdComboBox, generateBtn, feedbackLabel);
    }

    private void loadInvoiceIds() {
        List<Invoice> invoices = invoiceController.listAllInvoices();
        for (Invoice invoice : invoices) {
            invoiceIdComboBox.getItems().add(invoice.getId());
        }
    }

    /**
     * Generates a PDF for the given invoice.
     *
     * @param dest    The destination file path.
     * @param invoice The invoice to generate the PDF for.
     * @throws IOException If an I/O error occurs.
     */
    private void createPdf(String dest, Invoice invoice) throws IOException {
        // Initialize PDF writer
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add content to PDF
            document.add(new Paragraph("Invoice Report").setBold().setFontSize(18));
            document.add(new Paragraph("Generated at: " + LocalDateTime.now()));
            document.add(new Paragraph("----"));

            document.add(new Paragraph("Invoice ID: " + invoice.getId()));
            document.add(new Paragraph("Student Name: " + invoice.getStudent().getName()));
            document.add(new Paragraph("Student Email: " + invoice.getStudent().getEmail()));
            document.add(new Paragraph("Course Name: " + invoice.getCourse().getName()));
            document.add(new Paragraph("Course Fee: $" + invoice.getCourse().getFee()));
            document.add(new Paragraph("Discount: $" + invoice.getDiscount()));
            document.add(new Paragraph("Total Amount: $" + invoice.getTotalAmount()));
            document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate()));

            // Close document
            document.close();
        }
    }
}
