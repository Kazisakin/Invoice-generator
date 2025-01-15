package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WelcomePanel extends VBox {
    public WelcomePanel(){
        setSpacing(30);
        setPadding(new Insets(35));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #BDC3C7;");
        Label lbl=new Label("Welcome to the Student Invoice Management System");
        lbl.setStyle("-fx-font-size:24px; -fx-text-fill:#2C3E50; -fx-font-weight:bold;");
        getChildren().add(lbl);
    }
}
