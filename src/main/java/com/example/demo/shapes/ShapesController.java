package com.example.demo.shapes;

import com.example.demo.mousePressRelease.MousePressReleaseController;
import com.example.demo.opacity.OpacityController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.HashMap;
import java.util.Map;

public class ShapesController {
    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private ComboBox<String> opacityComboBox;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Pane drawPane;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider sliderWight;


    //TODO make the shapes when they are dragged and dropped to not go outside the Pane border
    //TODO after creating a line to stop making ones

    private final MousePressReleaseController mousePressReleaseController;
    private final OpacityController opacityController;

    private final Map<Node, Double[]> shapePositions = new HashMap<>();

    public ShapesController(MousePressReleaseController mousePressReleaseController, OpacityController opacityController) {
        this.mousePressReleaseController = mousePressReleaseController;
        this.opacityController = opacityController;
    }

    @FXML
    private void initialize() {
        configureDrawPane();
        setDrawPaneBackground();

        drawPane.setOnMouseClicked(this::handleMouseClicked);
        toggleButton.setOnAction(event -> toggleDragAndDrop());
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

    private Color changeColor() {
        return colorPicker.getValue();
    }

    private void handleMouseClicked(MouseEvent event) {
        String selectedShape = shapeComboBox.getValue();
        if (selectedShape != null) {
            switch (selectedShape) {
                case "Line":
                    mousePressReleaseController.setOnMousePressed(drawPane, shapeComboBox, this::createLine);
                    break;
                case "Dot":
                    createDot(event.getX(), event.getY(), changeColor());
                    break;
                case "Square":
                    createShape(event.getX(), event.getY(), 30, 30, changeColor());
                    break;
                case "Rectangle":
                    createShape(event.getX(), event.getY(), 60, 30, changeColor());
                    break;
                case "Triangle":
                    createTriangle(event.getX(), event.getY(), changeColor());
                    break;
                case "Circle":
                    createCircle(event.getX(), event.getY(), changeColor());
                    break;
                case "Oval":
                    createOval(event.getX(), event.getY(), changeColor());
                    break;
            }
        }
    }

    private double lineAndDotWight() {
        System.out.println(sliderWight.getValue());
        return sliderWight.getValue();
    }

    private void createLine(MouseEvent event) {
        double startX = event.getX();
        double startY = event.getY();
        mousePressReleaseController.setOnMouseReleased(drawPane, shapeComboBox, event2 -> {
            double endX = event2.getX();
            double endY = event2.getY();

            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(changeColor());
            line.setStrokeWidth(lineAndDotWight());
            drawPane.getChildren().add(line);
        });
    }

    private void createDot(double x, double y, Color color) {
        Circle dot = new Circle(x, y, lineAndDotWight());
        dot.setFill(color);
        drawPane.getChildren().add(dot);
    }

    //Start of methods for drag and drop shapes

    private void toggleDragAndDrop() {
        if (toggleButton.isSelected()) {
            for (Node node : drawPane.getChildren()) {
                if (node instanceof Shape) {
                    enableDragAndDrop((Shape) node);
                }
            }
            drawPane.setOnMouseClicked(null);
        } else {
            for (Node node : drawPane.getChildren()) {
                if (node instanceof Shape) {
                    disableDragAndDrop((Shape) node);
                }
            }
            drawPane.setOnMouseClicked(this::handleMouseClicked);
        }
    }

    private void enableDragAndDrop(Shape shape) {
        shape.setOnMousePressed(event -> {
            shapePositions.put(shape, new Double[]{event.getSceneX(), event.getSceneY()});
        });

        shape.setOnMouseDragged(event -> {
            Double[] position = shapePositions.get(shape);

            if (position != null) {
                double offsetX = event.getSceneX() - position[0];
                double offsetY = event.getSceneY() - position[1];

                double newTranslateX = offsetX + shape.getTranslateX();
                double newTranslateY = offsetY + shape.getTranslateY();

                shape.setTranslateX(newTranslateX);
                shape.setTranslateY(newTranslateY);

                shapePositions.put(shape, new Double[]{event.getSceneX(), event.getSceneY()});
            }
        });

        shape.setOnMouseReleased(event -> {
            shapePositions.remove(shape);
        });
    }

    private void disableDragAndDrop(Shape shape) {
        shape.setOnMousePressed(null);
        shape.setOnMouseDragged(null);
        shape.setOnMouseReleased(null);
    }

    //End of methods for drag and drop shapes

    private double opacity() {
        return opacityController.getOpacityComboBox(opacityComboBox);
    }

    private void createShape(double x, double y, double width, double height, Color color) {
        Rectangle shape = new Rectangle(x, y, width, height);
        color = color.deriveColor(0, 1, 1, opacity());
        shape.setFill(color);
        drawPane.getChildren().add(shape);
    }

    private void createTriangle(double x, double y, Color color) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(x, y + 30, x + 30, y + 30, x + 15, y);
        color = color.deriveColor(0, 1, 1, opacity());
        triangle.setFill(color);
        drawPane.getChildren().add(triangle);
    }

    private void createCircle(double x, double y, Color color) {
        Circle circle = new Circle(x, y, 15);
        color = color.deriveColor(0, 1, 1, opacity());
        circle.setFill(color);
        drawPane.getChildren().add(circle);
    }

    private void createOval(double x, double y, Color color) {
        Ellipse ellipse = new Ellipse(x + 30, y + 15, 30, 15);
        color = color.deriveColor(0, 1, 1, opacity());
        ellipse.setFill(color);
        drawPane.getChildren().add(ellipse);
    }
}
