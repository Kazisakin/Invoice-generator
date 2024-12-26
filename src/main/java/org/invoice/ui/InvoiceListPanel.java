package org.invoice.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;
import org.invoice.repository.InvoiceRepository;
import org.invoice.repository.InvoiceRepositoryImpl;
import org.invoice.service.InvoiceService;

import java.util.List;

public class InvoiceListPanel extends VBox {

    private final InvoiceController invoiceController;
    private TableView<Invoice> tableView;

    public InvoiceListPanel() {
        // Initialize repository and service
        InvoiceRepository invoiceRepo = new InvoiceRepositoryImpl();
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);
        this.invoiceController = new InvoiceController(invoiceService);

        initUI();
    }

    private void initUI() {
        setSpacing(20);
        setPadding(new Insets(20));

        Label title = new Label("All Invoices");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tableView = new TableView<>();
        setupTableColumns();
        loadInvoices();

        getChildren().addAll(title, tableView);
    }

    private void setupTableColumns() {
        TableColumn<Invoice, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Invoice, String> studentNameCol = new TableColumn<>("Student Name");
        studentNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudent().getName()));

        TableColumn<Invoice, String> courseNameCol = new TableColumn<>("Course Name");
        courseNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCourse().getName()));

        TableColumn<Invoice, Double> feeCol = new TableColumn<>("Fee");
        feeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getCourse().getFee()).asObject());

        TableColumn<Invoice, Double> discountCol = new TableColumn<>("Discount");
        discountCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getDiscount()).asObject());

        TableColumn<Invoice, Double> totalCol = new TableColumn<>("Total Amount");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalAmount()).asObject());

        TableColumn<Invoice, String> dateCol = new TableColumn<>("Invoice Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getInvoiceDate().toString()));

        tableView.getColumns().addAll(idCol, studentNameCol, courseNameCol, feeCol, discountCol, totalCol, dateCol);
    }

    private void loadInvoices() {
        List<Invoice> invoices = invoiceController.listAllInvoices();
        ObservableList<Invoice> data = FXCollections.observableArrayList(invoices);
        tableView.setItems(data);
    }
}
