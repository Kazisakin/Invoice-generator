// src/main/java/org/invoice/ui/PdfPanel.java (Optional if you want PDF generation)
package org.invoice.ui;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;
import java.io.FileNotFoundException;

public class PdfPanel extends VBox {
    private final InvoiceController invCtrl;
    public PdfPanel(InvoiceController c){
        invCtrl=c;
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        Label title=new Label("Generate PDF");
        TextField idField=new TextField();idField.setPromptText("Invoice ID");
        Button genBtn=new Button("Generate");
        Label feedback=new Label();
        genBtn.setOnAction(e->{
            try{
                Long id=Long.parseLong(idField.getText());
                Invoice inv=invCtrl.findInvoiceById(id);
                if(inv==null){ feedback.setText("Invoice not found");return;}
                FileChooser fc=new FileChooser();
                fc.setTitle("Save PDF");
                fc.setInitialFileName("Invoice_"+id+".pdf");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF","*.pdf"));
                java.io.File file=fc.showSaveDialog(getScene().getWindow());
                if(file==null)return;
                PdfWriter w=new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf=new PdfDocument(w);
                Document doc=new Document(pdf);
                doc.add(new Paragraph("Invoice #"+inv.getId()));
                doc.add(new Paragraph("Student: "+(inv.getStudent()!=null ? inv.getStudent().getName() : "None")));
                doc.add(new Paragraph("Subtotal: "+inv.getSubtotal()));
                doc.add(new Paragraph("Discount: "+inv.getDiscount()));
                doc.add(new Paragraph("Tax: "+inv.getTaxAmount()));
                doc.add(new Paragraph("Total: "+inv.getTotal()));
                doc.add(new Paragraph("Issued: "+inv.getDateIssued()));
                doc.add(new Paragraph("Due: "+inv.getDueDate()));
                doc.close();
                feedback.setText("PDF saved: "+file.getAbsolutePath());
            }catch(NumberFormatException ex){ feedback.setText("Invalid ID");}
            catch(FileNotFoundException ex){ feedback.setText("File error: "+ex.getMessage());}
            catch(Exception ex){ feedback.setText("Error: "+ex.getMessage());}
        });
        getChildren().addAll(title, idField, genBtn, feedback);
    }
}
