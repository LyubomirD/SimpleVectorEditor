package com.example.demo;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainController {
    /**
     *
     * Choose size and color of the shape beforehand
     * Choose the shape and opacity beforehand
     * !Opacity by default is 100% meaning NO OPACITY
     *
     *
     * To move shape individually:
     * 1. Press SHIFT and hold
     * 2. Double right-click it
     * 3. Drag it without releasing the right button and
     * 4. Drop it by releasing the right-button
     * 5. Click on any shape to turn OFF selection
     *
     * To move multiple shapes:
     * 1. Press SHIFT and hold
     * 2. Right-click all the shapes you want to move
     * 3. Right-click the last shape you want to move and hold it
     * 4. You will drag the shapes as one
     * 5. Release the right button to drop them into position
     * 6. Click on any shape to turn OFF selection
     *
     * To rotate multiple of individual shapes:
     * 1. Press SHIFT
     * 2. Select the shape/shapes with the right-click
     * 3. Release SHIFT or not you choose
     * 4. Move the Angle Slider or use the left and right arrow keys
     * 5. Click on any shape to turn OFF selection
     *
     * */


    //Start FXML components
    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private ComboBox<String> opacityComboBox;

    @FXML
    private Text selectionStatusText;

    @FXML
    private Pane drawPane;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider sliderSize;

    @FXML
    private Slider sliderAngle;

    @FXML
    private Text angleDisplay;

    @FXML
    private Text sizeDisplay;

    @FXML
    private Button clearPaneButton;

    @FXML
    private Button download;
    //End FXML components

    //Start Global Variables
    private double startX, startY;
    private final List<Node> selectedShapes = new ArrayList<>();
    private double rotationAngle = 0.0;
    private double proportions = 1.0;
    //End Global Variables


    //Start Initialize method
    @FXML
    private void initialize() {
        configureDrawPane();
        setDrawPaneBackground();

        // Shape creation and selection
        drawPane.setOnMouseClicked(this::handleMouseClicked);
        drawPane.setOnMouseDragged(this::handleShapeMouseDragged);

        // Slider listeners
        sliderAngle.valueProperty().addListener((observable, oldValue, newValue) -> {
            rotationAngle = newValue.intValue();
            rotateSelectedShapes();
            angleDisplay.setText("Angle: " + rotationAngle + "°");
        });

        sliderSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            proportions = newValue.doubleValue();
            sizeDisplay.setText("Size: " + newValue.intValue());
        });

        // Erase functionality
        clearPaneButton.setOnAction(this::clearTheWholePane);

        // Download pane
        download.setOnAction(this::captureAndDownloadPane);
    }

    //End Initialize method


    //Start Configure pane
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
    //End Configure pane


    //Start Choose color
    private Color changeColor() {
        return colorPicker.getValue();
    }
    //End Choose color


    //Start choose shape size
    private double shapesSizeChange() {
        return proportions;
    }
    //End choose shape size


    //Start rotation of shapes
    private void rotateSelectedShapes() {
        for (Node shape : selectedShapes) {
            double centerX = shape.getBoundsInLocal().getWidth() / 2.0 + shape.getBoundsInLocal().getMinX();
            double centerY = shape.getBoundsInLocal().getHeight() / 2.0 + shape.getBoundsInLocal().getMinY();
            shape.getTransforms().clear();
            shape.getTransforms().add(new Rotate(rotationAngle, centerX, centerY));
        }
    }
    //End rotation of shapes


    //Start Choose opacity
    private double opacity() {
        String opacityText = opacityComboBox.getValue();
        if (opacityText != null) {
            double opacity = Double.parseDouble(opacityText.replace("%", "")) / 100.0;
            return opacity;
        } else {
            return 1.0;
        }
    }
    //End Choose opacity


    //Start Choose shape from combobox
    private void handleMouseClicked(MouseEvent event) {
        String selectedShape = shapeComboBox.getValue();
        if (selectedShape != null) {
            switch (selectedShape) {
                case "Select a shape":
                    break;
                case "Line":
                    createLinePosition();
                    break;
                case "Dot":
                    createDot(event.getX(), event.getY(), changeColor());
                    break;
                case "Square":
                    createSquareAndRectangle(event.getX(), event.getY(), shapesSizeChange(), shapesSizeChange(), changeColor());
                    break;
                case "Rectangle":
                    createSquareAndRectangle(event.getX(), event.getY(), shapesSizeChange() * 2, shapesSizeChange(), changeColor());
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
    //End Choose shape from combobox


    //Start create line and dot
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
        startX = clamp(startX, 0, drawPane.getWidth());
        startY = clamp(startY, 0, drawPane.getHeight());
        endX = clamp(endX, 0, drawPane.getWidth());
        endY = clamp(endY, 0, drawPane.getHeight());

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(changeColor());
        line.setStrokeWidth(shapesSizeChange());
        line.setOnMousePressed(event -> handleShapeMousePressed(event, line));
        drawPane.getChildren().add(line);
    }

    private void createDot(double x, double y, Color color) {
        x = clamp(x, 0, drawPane.getWidth());
        y = clamp(y, 0, drawPane.getHeight());

        Circle dot = new Circle(x, y, shapesSizeChange());
        dot.setFill(color);
        dot.setOnMousePressed(event -> handleShapeMousePressed(event, dot));
        drawPane.getChildren().add(dot);
    }
    //End create line and dot


    //Start drag and drop
    private void handleShapeMousePressed(MouseEvent event, Node shape) {
        if (event.isShiftDown()) {
            shapeComboBox.setValue("Select a shape");
            selectionStatusText.setText("Selection is: ON");
            toggleSelection(shape);
        } else {
            selectedShapes.clear();
            selectionStatusText.setText("Selection is: OFF");
            sliderAngle.setValue(0);
        }
        startX = event.getX();
        startY = event.getY();
    }

    private void toggleSelection(Node shape) {
        if (!selectedShapes.contains(shape)) {
            selectedShapes.add(shape);
        }
    }


    private void handleShapeMouseDragged(MouseEvent event) {
        if (!selectedShapes.isEmpty() && event.getButton() != MouseButton.SECONDARY) {
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
    //End drag and drop


    //Start creating/making shapes
    private void createSquareAndRectangle(double x, double y, double width, double height, Color color) {
        x = clamp(x, 0, drawPane.getWidth());
        y = clamp(y, 0, drawPane.getHeight());

        Rectangle square = new Rectangle(x, y, width, height);
        square.setRotate(sliderAngle.getValue());
        color = color.deriveColor(0, 1, 1, opacity());
        square.setFill(color);
        square.setOnMousePressed(event -> handleShapeMousePressed(event, square));
        drawPane.getChildren().add(square);
    }

    private void createTriangle(double x, double y, double size, Color color) {
        x = clamp(x, 0, drawPane.getWidth());
        y = clamp(y, 0, drawPane.getHeight());

        Polygon triangle = new Polygon();
        triangle.setRotate(sliderAngle.getValue());
        triangle.getPoints().addAll(x, y + size, x + size, y + size, x + size / 2, y);
        color = color.deriveColor(0, 1, 1, opacity());
        triangle.setFill(color);
        triangle.setOnMousePressed(event -> handleShapeMousePressed(event, triangle));
        drawPane.getChildren().add(triangle);
    }

    private void createCircle(double x, double y, double size, Color color) {
        x = clamp(x, 0, drawPane.getWidth());
        y = clamp(y, 0, drawPane.getHeight());

        Circle circle = new Circle(x, y, size / 2);
        color = color.deriveColor(0, 1, 1, opacity());
        circle.setFill(color);
        circle.setOnMousePressed(event -> handleShapeMousePressed(event, circle));
        drawPane.getChildren().add(circle);
    }

    private void createOval(double x, double y, double size, Color color) {
        x = clamp(x, 0, drawPane.getWidth());
        y = clamp(y, 0, drawPane.getHeight());

        Ellipse ellipse = new Ellipse(x + size, y + size / 2, size, size / 2);
        ellipse.setRotate(sliderAngle.getValue());
        color = color.deriveColor(0, 1, 1, opacity());
        ellipse.setFill(color);
        ellipse.setOnMousePressed(event -> handleShapeMousePressed(event, ellipse));
        drawPane.getChildren().add(ellipse);
    }
    //End creating/making shapes


    // Start clamp a value between a minimum and maximum
    private double clamp(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }
    // End clamp a value between a minimum and maximum


    //Start clear pane
    private void clearTheWholePane(ActionEvent event) {
        if (event.getSource() == clearPaneButton) {
            drawPane.getChildren().clear();
        }
    }
    //End clear pane


    //Start download pane
    private void captureAndDownloadPane(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            savePaneAsImage(drawPane, file);
        }
    }

    private void savePaneAsImage(Pane pane, File file) {
        WritableImage writableImage = pane.snapshot(new SnapshotParameters(), null);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //End download pane
}
