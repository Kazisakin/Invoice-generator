package org.invoice.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A simple welcome panel displayed as the 'Home' view.
 */
public class WelcomePanel extends VBox {

    public WelcomePanel() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label title = new Label("Welcome to the Invoice Management System!");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Use the navigation buttons above to manage invoices.");
        subtitle.setStyle("-fx-font-size: 14px;");

        getChildren().addAll(title, subtitle);
    }
}
