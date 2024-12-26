package org.invoice.ui;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A JavaFX panel to generate a PDF invoice using iText 7.
 */
public class PdfPanel extends VBox {

    public PdfPanel() {
        setPadding(new Insets(10));
        setSpacing(10);

        Label title = new Label("Generate PDF");
        Button generateBtn = new Button("Export an Invoice to PDF");

        generateBtn.setOnAction(e -> {
            try {
                // Open a FileChooser to select where to save the PDF
                Stage stage = (Stage) this.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Invoice PDF");
                fileChooser.setInitialFileName("invoice.pdf");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );

                // Show the "Save" dialog
                java.io.File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    // Create the PDF file at the chosen location
                    createPdf(file.getAbsolutePath());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        getChildren().addAll(title, generateBtn);
    }

    /**
     * Creates a simple PDF invoice using iText 7.
     *
     * @param dest The full path (including filename) where the PDF will be saved.
     */
    private void createPdf(String dest) throws IOException {
        // Initialize PDF writer
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Example invoice content (replace with real data)
            document.add(new Paragraph("Invoice Report"));
            document.add(new Paragraph("Generated at: " + LocalDateTime.now()));
            document.add(new Paragraph("----"));
            document.add(new Paragraph("Item 1: $10\nItem 2: $20\nTotal: $30"));

            // Close document
            document.close();
        }
    }
}
