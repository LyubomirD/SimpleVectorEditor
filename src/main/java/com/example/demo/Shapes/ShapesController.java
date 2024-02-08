package com.example.demo.Shapes;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import java.util.Random;


public class ShapesController {
    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private Canvas drawCanvas;

    private double startX, startY;

    @FXML
    private void initialize() {
        GraphicsContext graphicsContext = drawCanvas.getGraphicsContext2D();
        double canvasWidth = drawCanvas.getWidth();
        double canvasHeight = drawCanvas.getHeight();

        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);

        drawCanvas.setOnMouseClicked(event -> {
            String selectedShape = shapeComboBox.getValue();
            if (selectedShape != null) {
                switch (selectedShape) {
                    case "Square":
                        createSquare(event.getX(), event.getY());
                        break;
                        case "Rectangle":
                        createRectangle(event.getX(), event.getY());
                        break;
                    case "Triangle":
                        createTriangle(event.getX(), event.getY());
                        break;
                    case "Circle":
                        createCircle(event.getX(), event.getY());
                        break;
                    case "Oval":
                        createOval(event.getX(), event.getY());
                        break;
                    case "Abstract":
                        createRandomAbstractShapes(event.getX(), event.getY());
                        break;
                }
            }
        });

        drawCanvas.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
        });

        drawCanvas.setOnMouseReleased(event -> {
            String selectedShape = shapeComboBox.getValue();
            if (selectedShape != null) {
                switch (selectedShape) {
                    case "Line":
                        createLine(startX, startY, event.getX(), event.getY());
                        break;
                    case "Dot":
                        createDot(event.getX(), event.getY());
                        break;
                }

            }
        });

    }

    private void createLine(double startX, double startY, double endX, double endY) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void createDot(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        double dotSize = 2;
        gc.fillOval(x - dotSize / 2, y - dotSize / 2, dotSize, dotSize);
    }

    private void createSquare(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(x, y, 30, 30);
    }

    private void createRectangle(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setFill(Color.PINK);
        gc.fillRect(x, y, 60, 30);
    }

    private void createTriangle(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);

        double[] xPoints = {x, x + 30, x + 15};
        double[] yPoints = {y + 30, y + 30, y};

        gc.fillPolygon(xPoints, yPoints, 3);
    }

    private void createCircle(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRoundRect(x, y, 30, 30, 30, 30);
    }

    private void createOval(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setFill(Color.ORANGE);
        gc.fillRoundRect(x, y, 60, 30, 60, 30);
    }


    private void createRandomAbstractShapes(double x, double y) {
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        Random random = new Random();

        Color color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
        gc.setFill(color);

        int minSides = 4;
        int maxSides = 10;
        int polygonSides = random.nextInt(maxSides - minSides + 1) + minSides;

        double[] xPoints = new double[polygonSides];
        double[] yPoints = new double[polygonSides];

        for (int i = 0; i < polygonSides; i++) {
            xPoints[i] = x + random.nextDouble() * 100;
            yPoints[i] = y + random.nextDouble() * 100;
        }

        gc.fillPolygon(xPoints, yPoints, polygonSides);
    }

    private void rotateShapes() {
    }

}
