package com.example.wallbuilding;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickWallPageController {

    @FXML private Pane wallPane;
    private final Random random = new Random();

    private BrickWall wall;

    private List<Stride> optimalStrides;
    private final List<List<Rectangle>> rectangleList = new ArrayList<>();
    private static final double SCALING_VALUE = 0.25;
    BrickAlgorithm algorithm;

    public void initializeWall(BrickWall wall) {
        this.wall = wall;
        this.algorithm = new BrickAlgorithm(wall);
        this.optimalStrides = algorithm.findBestStrideBFS();
        createWallVisualization();

        setupKeyHandling();
        System.out.println("Wall initialized with " + optimalStrides.size() + " optimal strides");
    }

    public void createWallVisualization() {
        int panelWidth = (int)(wall.wallWidth * SCALING_VALUE);
        int panelHeight = (int)(wall.wallHeight * SCALING_VALUE);

        wallPane.setPrefSize(panelWidth, panelHeight);

        drawWall();
        wallPane.requestFocus();
    }
    private void drawWall(){
        int offsetX = (int) (10 * SCALING_VALUE);
        int offsetY = (int) (10 * SCALING_VALUE);

        for (int i = 0; i < wall.rows; i++){
            List<Rectangle> currentRow = new ArrayList<>();
            double currentX = offsetX;
            for (int j = 0; j < wall.columns; j++){

                Brick brick = (wall.wallMatrix[i][j] == 1) ? wall.halfBrick : wall.fullBrick;
                double y = offsetY + i * brick.calculateCourseHeight(wall.bedJoint) * SCALING_VALUE;
                Rectangle rectangle = new Rectangle(currentX, y, brick.length * SCALING_VALUE, brick.height * SCALING_VALUE);
                rectangle.setFocusTraversable(false);

                currentX += (brick.length + wall.headJoint) * SCALING_VALUE;

                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.TRANSPARENT);
                wallPane.getChildren().add(rectangle);
                currentRow.add(rectangle);
            }
            rectangleList.add(currentRow);
        }
        wallPane.requestFocus();
    }

    private void setupKeyHandling() {
        wallPane.setFocusTraversable(true);
        wallPane.setOnKeyPressed(this::handleKeyPress);

        wallPane.setOnMouseClicked(event -> {
            wallPane.requestFocus();
        });

        wallPane.requestFocus();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println("Enter key pressed");
            colorNextStride();
        }
    }

    @FXML
    void pressedKey(KeyEvent event) {
        handleKeyPress(event);
    }

    private void colorNextStride() {
        int currentStrideIndex = 0;
        if (currentStrideIndex >= optimalStrides.size()) {
            System.out.println("All strides have been colored!");
            return;
        }

        Stride currStride = optimalStrides.get(currentStrideIndex);
        Color strideColor = generateRandomColor();

        int endRow = Math.min(currStride.startRow + algorithm.maxRowsPerStride, wall.rows);
        int endCol = Math.min(currStride.startCol + algorithm.maxColumnsPerStride, wall.columns);

        for (int i = currStride.startRow; i < endRow; i++){
            for(int j = currStride.startCol; j < endCol; j++){
                if(i < wall.rows && j < wall.columns){
                    Rectangle brick = rectangleList.get(i).get(j);
                    if (brick.getFill() == Color.TRANSPARENT) {
                        brick.setFill(strideColor);
                    }
                }
            }
        }
    }


    private Color generateRandomColor() {
        float hue = random.nextFloat() * 360;
        float saturation = (random.nextInt(2000) + 1000) / 10000f;
        float luminance = 0.9f;

        return Color.hsb(hue, saturation, luminance);
    }
}