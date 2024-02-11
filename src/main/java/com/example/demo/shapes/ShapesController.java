package com.example.demo.shapes;

import com.example.demo.mousePressRelease.MousePressReleaseController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class ShapesController {
    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private ComboBox<String> opacityComboBox;

    @FXML
    private Pane drawPane;

    private final MousePressReleaseController mousePressReleaseController;

    public ShapesController(MousePressReleaseController mousePressReleaseController) {
        this.mousePressReleaseController = mousePressReleaseController;
    }

    @FXML
    private void initialize() {
        configureDrawPane();
        setDrawPaneBackground();

        drawPane.setOnMouseClicked(this::handleMouseClicked);
    }

    private void configureDrawPane() {
        drawPane.setMaxSize(400, 400);
        drawPane.setPrefSize(400, 400);
        drawPane.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                null,
                null)));
    }

    private void setDrawPaneBackground() {
        drawPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    private void handleMouseClicked(MouseEvent event) {
        String selectedShape = shapeComboBox.getValue();
        if (selectedShape != null) {
            switch (selectedShape) {
                case "Line":
                    mousePressReleaseController.setOnMousePressed(drawPane, shapeComboBox, this::createLine);
                    break;
                case "Dot":
                    createDot(event.getX(), event.getY());
                    break;
                case "Square":
                    createShape(event.getX(), event.getY(), 30, 30, Color.RED);
                    break;
                case "Rectangle":
                    createShape(event.getX(), event.getY(), 60, 30, Color.BLUE);
                    break;
                case "Triangle":
                    createTriangle(event.getX(), event.getY(), Color.GREEN);
                    break;
                case "Circle":
                    createCircle(event.getX(), event.getY(), Color.ORANGE);
                    break;
                case "Oval":
                    createOval(event.getX(), event.getY(), Color.PINK);
                    break;
            }
        }
    }

    private void createLine(MouseEvent event) {
        double startX = event.getX();
        double startY = event.getY();
        mousePressReleaseController.setOnMouseReleased(drawPane, shapeComboBox, event2 -> {
            double endX = event2.getX();
            double endY = event2.getY();
            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            drawPane.getChildren().add(line);
        });
    }

    private void createDot(double x, double y) {
        Circle dot = new Circle(x, y, 1);
        dot.setFill(Color.BLACK);
        drawPane.getChildren().add(dot);
    }

    private double getOpacityComboBox() {
        String opacityText = opacityComboBox.getValue();
        if (opacityText != null) {
            double opacity = Double.parseDouble(opacityText.replace("%", "")) / 100.0;
            return opacity;
        } else {
            return 1.0;
        }
    }

    private void createShape(double x, double y, double width, double height, Color color) {
        Rectangle shape = new Rectangle(x, y, width, height);
        color = color.deriveColor(0, 1, 1, getOpacityComboBox());
        shape.setFill(color);
        drawPane.getChildren().add(shape);
    }

    private void createTriangle(double x, double y, Color color) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(x, y + 30, x + 30, y + 30, x + 15, y);
        color = color.deriveColor(0, 1, 1, getOpacityComboBox());
        triangle.setFill(color);
        drawPane.getChildren().add(triangle);
    }

    private void createCircle(double x, double y, Color color) {
        Circle circle = new Circle(x, y, 15);
        color = color.deriveColor(0, 1, 1, getOpacityComboBox());
        circle.setFill(color);
        drawPane.getChildren().add(circle);
    }

    private void createOval(double x, double y, Color color) {
        Ellipse ellipse = new Ellipse(x + 30, y + 15, 30, 15);
        color = color.deriveColor(0, 1, 1, getOpacityComboBox());
        ellipse.setFill(color);
        drawPane.getChildren().add(ellipse);
    }
}
