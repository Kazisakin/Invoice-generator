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
import org.invoice.repository.InvoiceRepository;
import org.invoice.repository.InvoiceRepositoryImpl;
import org.invoice.service.InvoiceService;

import java.util.Date;

public class InvoiceForm extends VBox {

    private final InvoiceController invoiceController;

    public InvoiceForm() {
        // Initialize repository and service
        InvoiceRepository invoiceRepo = new InvoiceRepositoryImpl();
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);
        this.invoiceController = new InvoiceController(invoiceService);

        initUI();
    }

    private void initUI() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Create New Invoice");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.setAlignment(Pos.CENTER);

        Label studentNameLabel = new Label("Student Name:");
        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Enter student name");

        Label studentEmailLabel = new Label("Student Email:");
        TextField studentEmailField = new TextField();
        studentEmailField.setPromptText("Enter student email");

        Label courseNameLabel = new Label("Course Name:");
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Enter course name");

        Label feeLabel = new Label("Course Fee:");
        TextField feeField = new TextField();
        feeField.setPromptText("Enter course fee");

        Label discountLabel = new Label("Discount:");
        TextField discountField = new TextField();
        discountField.setPromptText("Enter discount");

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

        Button saveBtn = new Button("Save Invoice");
        Label feedbackLabel = new Label();

        saveBtn.setOnAction(e -> {
            String studentName = studentNameField.getText().trim();
            String studentEmail = studentEmailField.getText().trim();
            String courseName = courseNameField.getText().trim();
            String feeText = feeField.getText().trim();
            String discountText = discountField.getText().trim();

            if (studentName.isEmpty() || studentEmail.isEmpty() || courseName.isEmpty() || feeText.isEmpty() || discountText.isEmpty()) {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Please fill in all fields.");
                return;
            }

            double fee, discount;
            try {
                fee = Double.parseDouble(feeText);
                discount = Double.parseDouble(discountText);
            } catch (NumberFormatException ex) {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Fee and Discount must be numeric.");
                return;
            }

            if (discount > fee) {
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Discount cannot exceed the course fee.");
                return;
            }

            // Create Student and Course objects
            Student student = new Student();
            student.setName(studentName);
            student.setEmail(studentEmail);

            Course course = new Course();
            course.setName(courseName);
            course.setFee(fee);

            // Create Invoice
            Invoice invoice = invoiceController.createInvoice(student, course, discount);
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Invoice created with ID: " + invoice.getId());

            // Optionally, clear the form
            studentNameField.clear();
            studentEmailField.clear();
            courseNameField.clear();
            feeField.clear();
            discountField.clear();
        });

        getChildren().addAll(title, formGrid, saveBtn, feedbackLabel);
    }
}
