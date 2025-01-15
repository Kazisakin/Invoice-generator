package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;
import org.invoice.domain.Student;

public class InvoiceForm extends VBox {
    private final InvoiceController invoiceController;

    public InvoiceForm(InvoiceController ctrl){
        invoiceController=ctrl;
        setSpacing(20);
        setPadding(new Insets(25));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #FFFFFF;");

        Label title=new Label("Create New Invoice");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #2C3E50; -fx-font-weight:bold;");

        TextField studentName=new TextField();
        studentName.setPromptText("Student Name");
        studentName.setMaxWidth(250);

        TextField subtotalField=new TextField();
        subtotalField.setPromptText("Subtotal");
        subtotalField.setMaxWidth(250);

        TextField discountField=new TextField();
        discountField.setPromptText("Discount");
        discountField.setMaxWidth(250);

        TextField taxRateField=new TextField();
        taxRateField.setPromptText("Tax Rate (0.07)");
        taxRateField.setMaxWidth(250);

        Button saveBtn=new Button("Save Invoice");
        saveBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill:white; -fx-font-weight:bold;");
        saveBtn.setPrefWidth(140);

        Label feedback=new Label();
        feedback.setStyle("-fx-text-fill: #2C3E50; -fx-font-size:14px;");

        saveBtn.setOnAction(e->{
            try{
                Student s=new Student();
                s.setName(studentName.getText().trim());
                double st=Double.parseDouble(subtotalField.getText().trim());
                double disc=Double.parseDouble(discountField.getText().trim());
                double tr=Double.parseDouble(taxRateField.getText().trim());
                Invoice inv=invoiceController.createInvoice(s, st, disc, tr);
                feedback.setText("Invoice created, ID="+inv.getId()+", Number="+inv.getInvoiceNumber());
            }catch(Exception ex){
                feedback.setText("Error: "+ex.getMessage());
            }
        });

        getChildren().addAll(title, studentName, subtotalField, discountField, taxRateField, saveBtn, feedback);
    }
}
