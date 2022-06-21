package com.example.phaseiv;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Window {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}