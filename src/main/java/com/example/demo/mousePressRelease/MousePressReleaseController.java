package com.example.demo.mousePressRelease;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MousePressReleaseController {

    public MousePressReleaseController() {
    }

    public void setOnMousePressed(Pane drawPane, ComboBox<String> shapeComboBox, MouseEventConsumer eventConsumer) {
        drawPane.setOnMousePressed(event -> {
            boolean pass = mousePositionLogic(event, shapeComboBox, drawPane);
            if (pass) {
                eventConsumer.accept(event);
            }
        });
    }

    public void setOnMouseReleased(Pane drawPane, ComboBox<String> shapeComboBox, MouseEventConsumer eventConsumer) {
        drawPane.setOnMouseReleased(event -> {
            boolean pass = mousePositionLogic(event, shapeComboBox, drawPane);
            if (pass) {
                eventConsumer.accept(event);
            }
        });
    }

    private boolean mousePositionLogic(MouseEvent event, ComboBox<String> shapeComboBox, Pane drawPane) {

        if (shapeComboBox.getValue() == null) {
            return false;
        }

        double positionX = event.getX();
        double positionY = event.getY();

        if (!(positionX <= drawPane.getMaxWidth() && positionX >= drawPane.getMinWidth())) {
            return false;
        }

        if (!(positionY <= drawPane.getMaxHeight() && positionY >= drawPane.getMinHeight())) {
            return false;
        }

        return true;
    }

    @FunctionalInterface
    public interface MouseEventConsumer {
        void accept(MouseEvent event);
    }
}
