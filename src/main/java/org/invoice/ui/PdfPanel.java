package org.invoice.ui;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;

import java.io.File;
import java.util.List;

public class PdfPanel extends VBox {
    private final InvoiceController invCtrl;

    public PdfPanel(InvoiceController c) {
        invCtrl = c;
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // UI Elements for generating a single invoice PDF
        Label title = new Label("Generate PDF");
        TextField idField = new TextField();
        idField.setPromptText("Invoice ID");
        Button genBtn = new Button("Generate");
        Label feedback = new Label();

        genBtn.setOnAction(e -> {
            try {
                Long id = Long.parseLong(idField.getText());
                Invoice inv = invCtrl.findInvoiceById(id);
                if (inv == null) {
                    feedback.setText("Invoice not found");
                    return;
                }
                FileChooser fc = new FileChooser();
                fc.setTitle("Save PDF");
                fc.setInitialFileName("Invoice_" + id + ".pdf");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = fc.showSaveDialog(getScene().getWindow());
                if (file == null) return;

                // Generate the PDF for the selected invoice
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document doc = new Document(pdf);

                // Add invoice details to the document
                doc.add(new Paragraph("Invoice #" + inv.getId()));
                doc.add(new Paragraph("Student: " + (inv.getStudent() != null ? inv.getStudent().getName() : "None")));
                doc.add(new Paragraph("Subtotal: " + inv.getSubtotal()));
                doc.add(new Paragraph("Discount: " + inv.getDiscount()));
                doc.add(new Paragraph("Tax: " + inv.getTaxAmount()));
                doc.add(new Paragraph("Total: " + inv.getTotal()));
                doc.add(new Paragraph("Issued: " + inv.getDateIssued()));
                doc.add(new Paragraph("Due: " + inv.getDueDate()));

                doc.close();
                feedback.setText("PDF saved: " + file.getAbsolutePath());
            } catch (NumberFormatException ex) {
                feedback.setText("Invalid ID");
            } catch (Exception ex) {
                feedback.setText("Error: " + ex.getMessage());
            }
        });

        getChildren().addAll(title, idField, genBtn, feedback);
    }

    // Generate a detailed invoice PDF for a single invoice
    public void generateInvoicePDF(Invoice invoice, Window window) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Invoice PDF");
            fileChooser.setInitialFileName("Invoice_" + invoice.getId() + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                // Add header
                document.add(new Paragraph("Invoice")
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Invoice #" + invoice.getInvoiceNumber())
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n"));

                // Add student details
                Table detailsTable = new Table(new float[]{3, 7});
                detailsTable.setWidth(UnitValue.createPercentValue(100));
                detailsTable.addCell(new Cell().add(new Paragraph("Student Name:").setBold()));
                detailsTable.addCell(new Cell().add(new Paragraph(invoice.getStudent().getName())));
                detailsTable.addCell(new Cell().add(new Paragraph("Date Issued:").setBold()));
                detailsTable.addCell(new Cell().add(new Paragraph(invoice.getDateIssued().toString())));
                detailsTable.addCell(new Cell().add(new Paragraph("Due Date:").setBold()));
                detailsTable.addCell(new Cell().add(new Paragraph(invoice.getDueDate().toString())));
                document.add(detailsTable);

                document.add(new Paragraph("\n"));

                // Add financial details
                Table financialTable = new Table(new float[]{5, 5});
                financialTable.setWidth(UnitValue.createPercentValue(100));
                financialTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));
                financialTable.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold()));
                financialTable.addCell(new Cell().add(new Paragraph("Subtotal:")));
                financialTable.addCell(new Cell().add(new Paragraph("$" + invoice.getSubtotal())));
                financialTable.addCell(new Cell().add(new Paragraph("Discount:")));
                financialTable.addCell(new Cell().add(new Paragraph("$" + invoice.getDiscount())));
                financialTable.addCell(new Cell().add(new Paragraph("Tax:")));
                financialTable.addCell(new Cell().add(new Paragraph("$" + invoice.getTaxAmount())));
                financialTable.addCell(new Cell().add(new Paragraph("Total:").setBold()));
                financialTable.addCell(new Cell().add(new Paragraph("$" + invoice.getTotal()).setBold()));
                document.add(financialTable);

                document.close();
                showAlert("Success", "Invoice PDF has been saved successfully!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Generate a detailed invoice list PDF for all invoices
    public void generateInvoiceListPDF(List<Invoice> invoices, Window window) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Invoice List PDF");
            fileChooser.setInitialFileName("Invoice_List.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                // Add title
                document.add(new Paragraph("Invoice List")
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Generated on: " + java.time.LocalDate.now())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n"));

                // Add table
                Table table = new Table(new float[]{2, 4, 4, 3, 3});
                table.setWidth(UnitValue.createPercentValue(100));
                table.addHeaderCell(new Cell().add(new Paragraph("Invoice #").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Student").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Issued").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Due Date").setBold()));

                for (Invoice invoice : invoices) {
                    table.addCell(new Cell().add(new Paragraph(invoice.getInvoiceNumber())));
                    table.addCell(new Cell().add(new Paragraph(invoice.getStudent().getName())));
                    table.addCell(new Cell().add(new Paragraph(invoice.getDateIssued().toString())));
                    table.addCell(new Cell().add(new Paragraph("$" + invoice.getTotal())));
                    table.addCell(new Cell().add(new Paragraph(invoice.getDueDate().toString())));
                }

                document.add(table);

                // Add summary
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("Total Invoices: " + invoices.size())
                        .setBold()
                        .setTextAlignment(TextAlignment.RIGHT));

                document.close();
                showAlert("Success", "Invoice List PDF has been saved successfully!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Utility to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
