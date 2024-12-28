package org.invoice.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;
import org.invoice.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InvoiceListPanel extends BorderPane {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceListPanel.class);

    private final InvoiceController invoiceController;
    private TableView<Invoice> invoiceTable;

    public InvoiceListPanel(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
        initUI();
    }

    private void initUI() {
        setPadding(new Insets(20));
        Label titleLabel = new Label("List of Invoices");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        setTop(titleLabel);
        BorderPane.setMargin(titleLabel, new Insets(0,0,10,0));

        invoiceTable = new TableView<>();
        setupColumns();
        loadInvoices();
        setCenter(invoiceTable);

        // Buttons (bottom)
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadInvoices());

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setOnAction(e -> deleteSelectedInvoice());

        ToolBar toolBar = new ToolBar(refreshBtn, deleteBtn);
        setBottom(toolBar);
    }

    private void setupColumns() {
        TableColumn<Invoice, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));

        TableColumn<Invoice, String> stuCol = new TableColumn<>("Student");
        stuCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStudent().getName())
        );

        TableColumn<Invoice, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCourse().getName())
        );

        TableColumn<Invoice, Number> discCol = new TableColumn<>("Discount");
        discCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getDiscount()));

        TableColumn<Invoice, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalAmount()));

        TableColumn<Invoice, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getInvoiceDate().toString())
        );

        invoiceTable.getColumns().addAll(idCol, stuCol, courseCol, discCol, totalCol, dateCol);
    }

    private void loadInvoices() {
        try {
            List<Invoice> invoices = invoiceController.listAllInvoices();
            ObservableList<Invoice> obsList = FXCollections.observableArrayList(invoices);
            invoiceTable.setItems(obsList);
            logger.info("Loaded {} invoices.", invoices.size());
        } catch (ServiceException ex) {
            logger.error("Failed to load invoices.", ex);
            showAlert("Error", "Failed to load invoices.");
        }
    }

    private void deleteSelectedInvoice() {
        Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "No invoice selected.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete invoice ID: " + selected.getId() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean deleted = invoiceController.deleteInvoice(selected.getId());
                    if (deleted) {
                        showAlert("Success", "Invoice deleted.");
                        loadInvoices();
                    } else {
                        showAlert("Error", "Could not delete invoice.");
                    }
                } catch (ServiceException e) {
                    logger.error("Error deleting invoice ID: {}", selected.getId(), e);
                    showAlert("Error", "Failed to delete invoice.");
                }
            }
        });
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
