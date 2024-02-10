package com.example.demo.Shapes;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

public class ShapesController {
    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private Pane drawPane;

    private double startX, startY;
    private double endX, endY;

    @FXML
    private void initialize() {
        drawPane.setMaxSize(400, 400);
        drawPane.setPrefSize(400, 400);

        BorderStroke borderStroke = new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                null,
                null
        );

        drawPane.setBorder(new Border(borderStroke));

        drawPane.setOnMousePressed(event -> {
            if (shapeComboBox.getValue() != null && shapeComboBox.getValue().equals("Line")) {
                startX = event.getX();
                startY = event.getY();
            }
        });

        drawPane.setOnMouseReleased(event -> {
            if (shapeComboBox.getValue() != null && shapeComboBox.getValue().equals("Line")) {
                double endX = event.getX();
                double endY = event.getY();
                createLine(startX, startY, endX, endY);
            }
        });

        drawPane.setOnMouseClicked(event -> {
            String selectedShape = shapeComboBox.getValue();
            switch (selectedShape) {
                case "Dot":
                    createDot(event.getX(), event.getY());
                    break;
                case "Square":
                    createSquare(event.getX(), event.getY(), Color.RED);
                    break;
                case "Rectangle":
                    createRectangle(event.getX(), event.getY(), Color.BLUE);
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
        });
    }

    private void createLine(double startX, double startY, double x, double y) {
        Line line = new Line(startX, startY, x, y);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);
        drawPane.getChildren().add(line);
    }

    private void createDot(double startX, double startY) {
        Circle dot = new Circle(startX, startY, 1);
        dot.setFill(Color.BLACK);
        drawPane.getChildren().add(dot);
    }

    private void createSquare(double x, double y, Paint fill) {
        Rectangle square = new Rectangle(x, y, 30, 30);
        square.setFill(fill);
        drawPane.getChildren().add(square);
    }

    private void createRectangle(double x, double y, Paint fill) {
        Rectangle rectangle = new Rectangle(x, y, 60, 30);
        rectangle.setFill(fill);
        drawPane.getChildren().add(rectangle);
    }

    private void createTriangle(double x, double y, Paint fill) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(x, y + 30, x + 30, y + 30, x + 15, y);
        triangle.setFill(fill);
        drawPane.getChildren().add(triangle);
    }

    private void createCircle(double x, double y, Paint fill) {
        Circle circle = new Circle(x, y, 15);
        circle.setFill(fill);
        drawPane.getChildren().add(circle);
    }

    private void createOval(double x, double y, Paint fill) {
        Ellipse ellipse = new Ellipse(x + 30, y + 15, 30, 15);
        ellipse.setFill(fill);
        drawPane.getChildren().add(ellipse);
    }
}

/*
drawingCanvas.setOnMousePressed(event -> {
        this.startX = event.getX();
        this.startY = event.getY();
        String selectedShape = shapeComboBox.getValue();
        if(selectedShape.equals("Polygon")){
            polygonPoints.add(new Point2D(this.startX, this.startY));
        }

        // Start the timeline when the user starts drawing
        drawTimeline.playFromStart();
    });

    drawingCanvas.setOnMouseDragged(event -> {
        double endX = event.getX();
        double endY = event.getY();

        // Draw the partial shape while the user is still dragging
        drawSelectedShape(startX, startY, endX, endY);
    });

    drawingCanvas.setOnMouseReleased(event -> {
        double endX = event.getX();
        double endY = event.getY();
        String selectedShape = shapeComboBox.getValue();
        if(selectedShape.equals("Polygon")){
            polygonPoints.add(new Point2D(startX, startY));
        }

        // Stop the timeline when the user releases the mouse
        drawTimeline.stop();
        drawSelectedShape(startX, startY, endX, endY);
    });





 */
