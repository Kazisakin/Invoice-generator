package org.invoice.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WelcomePanel extends StackPane {

    public WelcomePanel() {
        setAlignment(Pos.CENTER);
        Label welcomeLabel = new Label("Welcome to the Invoice Application!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2C3E50;");
        getChildren().add(welcomeLabel);
    }
}
