package org.invoice.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.invoice.controller.InvoiceController;
import org.invoice.domain.Invoice;

import java.util.List;

public class InvoiceListPanel extends BorderPane {
    private final InvoiceController invoiceController;
    private TableView<Invoice> table;

    public InvoiceListPanel(InvoiceController c){
        invoiceController=c;
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ECF0F1;");

        Label title=new Label("Invoice Records");
        title.setStyle("-fx-font-size:18px; -fx-text-fill:#2C3E50; -fx-font-weight:bold;");
        setTop(title);

        table=new TableView<>();
        setupColumns();
        loadData();
        setCenter(table);

        Button refresh=new Button("Refresh");
        refresh.setStyle("-fx-background-color:#2980B9; -fx-text-fill:white; -fx-font-weight:bold;");
        refresh.setOnAction(e->loadData());

        Button delete=new Button("Delete");
        delete.setStyle("-fx-background-color:#C0392B; -fx-text-fill:white; -fx-font-weight:bold;");
        delete.setOnAction(e->deleteSelected());

        ToolBar tb=new ToolBar(refresh, delete);
        setBottom(tb);
    }

    private void setupColumns(){
        TableColumn<Invoice,Number> idCol=new TableColumn<>("ID");
        idCol.setCellValueFactory(d->new SimpleLongProperty(d.getValue().getId()==null?0:d.getValue().getId()));

        TableColumn<Invoice,String> invNumCol=new TableColumn<>("Invoice #");
        invNumCol.setCellValueFactory(d->new javafx.beans.property.SimpleStringProperty(
                d.getValue().getInvoiceNumber()!=null ? d.getValue().getInvoiceNumber() : "N/A"
        ));

        TableColumn<Invoice,String> stuCol=new TableColumn<>("Student");
        stuCol.setCellValueFactory(d->new javafx.beans.property.SimpleStringProperty(
                (d.getValue().getStudent()!=null)? d.getValue().getStudent().getName() : "N/A"
        ));

        TableColumn<Invoice,Number> totalCol=new TableColumn<>("Total");
        totalCol.setCellValueFactory(d->new SimpleDoubleProperty(d.getValue().getTotal()));

        table.getColumns().addAll(idCol, invNumCol, stuCol, totalCol);
    }

    private void loadData(){
        List<Invoice> list=invoiceController.listAllInvoices();
        ObservableList<Invoice> obs=FXCollections.observableArrayList(list);
        table.setItems(obs);
    }

    private void deleteSelected(){
        Invoice sel=table.getSelectionModel().getSelectedItem();
        if(sel==null)return;
        invoiceController.deleteInvoice(sel.getId());
        loadData();
    }
}
