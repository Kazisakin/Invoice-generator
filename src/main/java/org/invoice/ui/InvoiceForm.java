package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Course;
import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceForm extends VBox {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceForm.class);

    private final InvoiceController invoiceController;

    public InvoiceForm(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
        initUI();
    }

    private void initUI() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Create New Invoice");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);

        Label studentNameLabel = new Label("Student Name:");
        TextField studentNameField = new TextField();

        Label studentEmailLabel = new Label("Student Email:");
        TextField studentEmailField = new TextField();

        Label courseNameLabel = new Label("Course Name:");
        TextField courseNameField = new TextField();

        Label feeLabel = new Label("Course Fee:");
        TextField feeField = new TextField();

        Label discountLabel = new Label("Discount:");
        TextField discountField = new TextField();

        Button saveButton = new Button("Save Invoice");
        Label feedbackLabel = new Label();

        formGrid.add(studentNameLabel, 0, 0);
        formGrid.add(studentNameField, 1, 0);
        formGrid.add(studentEmailLabel, 0, 1);
        formGrid.add(studentEmailField, 1, 1);
        formGrid.add(courseNameLabel, 0, 2);
        formGrid.add(courseNameField, 1, 2);
        formGrid.add(feeLabel, 0, 3);
        formGrid.add(feeField, 1, 3);
        formGrid.add(discountLabel, 0, 4);
        formGrid.add(discountField, 1, 4);
        formGrid.add(saveButton, 1, 5);

        getChildren().addAll(titleLabel, formGrid, feedbackLabel);

        saveButton.setOnAction(e -> {
            String stuName = studentNameField.getText().trim();
            String stuEmail = studentEmailField.getText().trim();
            String crsName = courseNameField.getText().trim();
            String feeTxt = feeField.getText().trim();
            String discTxt = discountField.getText().trim();

            if (stuName.isEmpty() || stuEmail.isEmpty() || crsName.isEmpty() || feeTxt.isEmpty() || discTxt.isEmpty()) {
                feedbackLabel.setText("Please fill all fields.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            double fee, discount;
            try {
                fee = Double.parseDouble(feeTxt);
                discount = Double.parseDouble(discTxt);
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Fee/Discount must be numeric!");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Student student = new Student();
            student.setName(stuName);
            student.setEmail(stuEmail);

            Course course = new Course();
            course.setName(crsName);
            course.setFee(fee);

            try {
                Invoice invoice = invoiceController.createInvoice(student, course, discount);
                if (invoice != null) {
                    feedbackLabel.setStyle("-fx-text-fill: green;");
                    feedbackLabel.setText("Invoice created! ID: " + invoice.getId());
                    logger.info("Invoice created: {}", invoice.getId());

                    // Clear fields
                    studentNameField.clear();
                    studentEmailField.clear();
                    courseNameField.clear();
                    feeField.clear();
                    discountField.clear();
                } else {
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                    feedbackLabel.setText("Failed to create invoice.");
                }
            } catch (ServiceException ex) {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Error: " + ex.getMessage());
                logger.error("Error creating invoice", ex);
            }
        });
    }
}
