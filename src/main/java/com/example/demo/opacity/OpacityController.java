package com.example.demo.opacity;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class OpacityController {

    public OpacityController() {
    }

    public double getOpacityComboBox(ComboBox<String> opacityComboBox) {
        String opacityText = opacityComboBox.getValue();
        if (opacityText != null) {
            double opacity = Double.parseDouble(opacityText.replace("%", "")) / 100.0;
            return opacity;
        } else {
            return 1.0;
        }
    }
}
