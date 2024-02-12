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
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO make the shapes when they are dragged and dropped to not go outside the Pane border

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

    @FXML
    private Text selectionStatusText;

    private double startX, startY;
    private final MousePressReleaseController mousePressReleaseController;
    private final OpacityController opacityController;
    private final Map<Node, Double[]> shapePositions = new HashMap<>();
    private final List<Node> selectedShapes = new ArrayList<>();

    public ShapesController(MousePressReleaseController mousePressReleaseController, OpacityController opacityController) {
        this.mousePressReleaseController = mousePressReleaseController;
        this.opacityController = opacityController;
    }

    @FXML
    private void initialize() {
        configureDrawPane();
        setDrawPaneBackground();

        drawPane.setOnMouseClicked(this::handleMouseClicked);
        drawPane.setOnMouseDragged(this::handleShapeMouseDragged);
        //toggleButton.setOnAction(event -> toggleDragAndDrop());
    }


    /**
     * Start Pane set up
     */
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
    /** End Pane set up */


    /**
     * Start Helping methods
     */
    private Color changeColor() {
        return colorPicker.getValue();
    }

    private double shapesSizeChange() {
        return sliderWight.getValue();
    }

    private double opacity() {
        return opacityController.getOpacityComboBox(opacityComboBox);
    }
    /** End Helping methods */


    /**
     * Start Switch Statement
     */
    private void handleMouseClicked(MouseEvent event) {
        String selectedShape = shapeComboBox.getValue();
        if (selectedShape != null) {
            switch (selectedShape) {
                case "Line":
                    createLinePosition();
                    break;
                case "Dot":
                    createDot(event.getX(), event.getY(), changeColor());
                    break;
                case "Square":
                    createShape(event.getX(), event.getY(), shapesSizeChange(), shapesSizeChange(), changeColor());
                    break;
                case "Rectangle":
                    createShape(event.getX(), event.getY(), shapesSizeChange() * 2, shapesSizeChange(), changeColor());
                    break;
                case "Triangle":
                    createTriangle(event.getX(), event.getY(), shapesSizeChange(), changeColor());
                    break;
                case "Circle":
                    createCircle(event.getX(), event.getY(), shapesSizeChange(), changeColor());
                    break;
                case "Oval":
                    createOval(event.getX(), event.getY(), shapesSizeChange(), changeColor());
                    break;
            }
        }
    }
    /** End Switch Statement */


    /**
     * Start Line code
     */
    private void createLinePosition() {
        drawPane.setOnMousePressed(event -> {
            if (shapeComboBox.getValue() != null && shapeComboBox.getValue().equals("Line") && mousePositionLogic(event)) {
                startX = event.getX();
                startY = event.getY();
            }
        });

        drawPane.setOnMouseReleased(event -> {
            if (shapeComboBox.getValue() != null && shapeComboBox.getValue().equals("Line") && mousePositionLogic(event)) {
                double endX = event.getX();
                double endY = event.getY();
                createLine(startX, startY, endX, endY);
            }
        });
    }

    private boolean mousePositionLogic(MouseEvent event) {
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

    private void createLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(changeColor());
        line.setStrokeWidth(shapesSizeChange());
        line.setOnMousePressed(event -> handleShapeMousePressed(event, line));
        drawPane.getChildren().add(line);
    }

    private void createDot(double x, double y, Color color) {
        Circle dot = new Circle(x, y, shapesSizeChange());
        dot.setFill(color);
        dot.setOnMousePressed(event -> handleShapeMousePressed(event, dot));
        drawPane.getChildren().add(dot);
    }
    /** End Line code */


//    /**
//     * Start of methods for drag and drop shapes
//     */
//    private void toggleDragAndDrop() {
//        if (toggleButton.isSelected()) {
//            for (Node node : drawPane.getChildren()) {
//                if (node instanceof Shape) {
//                    enableDragAndDrop((Shape) node);
//                }
//            }
//            drawPane.setOnMouseClicked(null);
//        } else {
//            for (Node node : drawPane.getChildren()) {
//                if (node instanceof Shape) {
//                    disableDragAndDrop((Shape) node);
//                }
//            }
//            drawPane.setOnMouseClicked(this::handleMouseClicked);
//        }
//    }
//
//    private void enableDragAndDrop(Shape shape) {
//        shape.setOnMousePressed(event -> {
//            shapePositions.put(shape, new Double[]{event.getSceneX(), event.getSceneY()});
//        });
//
//        shape.setOnMouseDragged(event -> {
//            Double[] position = shapePositions.get(shape);
//
//            if (position != null) {
//                double offsetX = event.getSceneX() - position[0];
//                double offsetY = event.getSceneY() - position[1];
//
//                double newTranslateX = offsetX + shape.getTranslateX();
//                double newTranslateY = offsetY + shape.getTranslateY();
//
//                shape.setTranslateX(newTranslateX);
//                shape.setTranslateY(newTranslateY);
//
//                shapePositions.put(shape, new Double[]{event.getSceneX(), event.getSceneY()});
//            }
//        });
//
//        shape.setOnMouseReleased(event -> {
//            shapePositions.remove(shape);
//        });
//    }
//
//    private void disableDragAndDrop(Shape shape) {
//        shape.setOnMousePressed(null);
//        shape.setOnMouseDragged(null);
//        shape.setOnMouseReleased(null);
//    }
//    /** End of methods for drag and drop shapes */


    /** Start of methods for multiple drag and drop shapes */
    private void toggleSelection(Node shape) {
        if (selectedShapes.contains(shape)) {
            selectedShapes.remove(shape);
        } else {
            selectedShapes.add(shape);
        }
    }


    private void handleShapeMousePressed(MouseEvent event, Node shape) {
        if (event.isShiftDown()) {
            toggleSelection(shape);
        } else {
            if (!selectedShapes.contains(shape)) {
                selectedShapes.clear();
                selectedShapes.add(shape);
            }
        }
        startX = event.getX();
        startY = event.getY();

        // Update selection status text
        if (event.isShiftDown()) {
            selectionStatusText.setText("Multiple selection is: ON");
        } else {
            selectionStatusText.setText("Multiple selection is: OFF");
        }
    }

    private void handleShapeMouseDragged(MouseEvent event) {
        if (!selectedShapes.isEmpty()) {
            double offsetX = event.getX() - startX;
            double offsetY = event.getY() - startY;
            for (Node shape : selectedShapes) {
                shape.setTranslateX(shape.getTranslateX() + offsetX);
                shape.setTranslateY(shape.getTranslateY() + offsetY);
            }
            startX = event.getX();
            startY = event.getY();
        }
    }

    /** End of methods for multiple drag and drop shapes */


    /**
     * Start method make Shapes
     */
    private void createShape(double x, double y, double width, double height, Color color) {
        Rectangle shape = new Rectangle(x, y, width, height);
        color = color.deriveColor(0, 1, 1, opacity());
        shape.setFill(color);
        shape.setOnMousePressed(event -> handleShapeMousePressed(event, shape));
        drawPane.getChildren().add(shape);
    }

    private void createTriangle(double x, double y, double size, Color color) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(x, y + size, x + size, y + size, x + size / 2, y);
        color = color.deriveColor(0, 1, 1, opacity());
        triangle.setFill(color);
        triangle.setOnMousePressed(event -> handleShapeMousePressed(event, triangle));
        drawPane.getChildren().add(triangle);
    }

    private void createCircle(double x, double y, double size, Color color) {
        Circle circle = new Circle(x, y, size / 2);
        color = color.deriveColor(0, 1, 1, opacity());
        circle.setFill(color);
        circle.setOnMousePressed(event -> handleShapeMousePressed(event, circle));
        drawPane.getChildren().add(circle);
    }

    private void createOval(double x, double y, double size, Color color) {
        Ellipse ellipse = new Ellipse(x + size, y + size / 2, size, size / 2);
        color = color.deriveColor(0, 1, 1, opacity());
        ellipse.setFill(color);
        ellipse.setOnMousePressed(event -> handleShapeMousePressed(event, ellipse));
        drawPane.getChildren().add(ellipse);
    }
    /** End method make Shapes */
}
