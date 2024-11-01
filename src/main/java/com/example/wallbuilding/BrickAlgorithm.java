package com.example.wallbuilding;

import java.util.*;

public class BrickAlgorithm {
    private final BrickWall wall;
    private final int rows;
    private final int columns;
    private static final int STRIDE_WIDTH = 800;
    private static final int STRIDE_HEIGHT = 1300;

    public int maxRowsPerStride;
    public int maxColumnsPerStride;

    public BrickAlgorithm(BrickWall wall) {
        this.wall = wall;
        this.rows = wall.rows;
        this.columns = wall.columns;
        this.maxRowsPerStride = wall.calculateNumberRows(STRIDE_HEIGHT, wall.courseHeight);
        this.maxColumnsPerStride = wall.calculateNumberColumns(STRIDE_WIDTH, wall.courseLength);
    }

    // Definitely not the fastest way to go through the possible solutions
    public List<Stride> findBestStrideBFS(){
        //Calculate number of rows and columns that we can cover with one stride

//        System.out.println("\n=== Starting BFS ===");
//        System.out.println("Max rows per stride: " + maxRowsPerStride);
//        System.out.println("Max columns per stride: " + maxColumnsPerStride);

        WallState initialState = new WallState(new int[rows], new ArrayList<>(), rows, columns);

        Stride initialStride = new Stride(0, 0);
        initialState = buildBricksInRange(initialState, initialStride);

        Queue<WallState> queue = new LinkedList<>();
        Set<String> visitedStates = new HashSet<>();

        queue.add(initialState);
        visitedStates.add(generateUniqueIDKey(initialState));

        WallState optimumSolution = null;

        while(!queue.isEmpty()){
            WallState currState = queue.poll();

            if(isWallComplete(currState)){
                System.out.println("Wall complete! Found solution with " + currState.stridesTaken.size() + " strides.");
                optimumSolution = currState;
                break;
            }

            List<Stride> possibleStrides = getAllPossibleStrides(currState);
            System.out.println("Found " + possibleStrides.size() + " possible next strides");

            for(Stride stride : possibleStrides){
                WallState newState = buildBricksInRange(currState, stride);
                String stateKey = generateUniqueIDKey(newState);

                if (!visitedStates.contains(stateKey)) {
                    visitedStates.add(stateKey);
                    queue.add(newState);
                }
            }
        }

        if (optimumSolution != null) {
            System.out.println("\n=== Solution Found ===");
            System.out.println("Total strides: " + optimumSolution.stridesTaken.size());
            printSolution(optimumSolution);
        }

        return optimumSolution != null ? optimumSolution.stridesTaken : new ArrayList<>();
    }

    private void printWallState(WallState state) {
        System.out.println("Positions built in each row:");
        for (int i = 0; i < state.rows; i++) {
            System.out.println("Row " + i + ": built up to column " + state.positionsBuiltBricks[i]);
        }
        System.out.println("Current number of strides taken: " + state.stridesTaken.size());
    }

    private void printSolution(WallState finalState) {
        System.out.println("\nStride sequence:");
        for (int i = 0; i < finalState.stridesTaken.size(); i++) {
            Stride stride = finalState.stridesTaken.get(i);
            System.out.println("Stride " + (i + 1) + ": (" + stride.startRow + ", " + stride.startCol + ")");
        }
    }

    private boolean isWallComplete(WallState wallState){
        for (int i = 0; i < wallState.positionsBuiltBricks.length; i++){
            if(wallState.positionsBuiltBricks[i] < columns - 1) {
                return false;
            }
        }
        return true;
    }

    private String generateUniqueIDKey(WallState state) {
        StringBuilder key = new StringBuilder();
        for (int pos : state.positionsBuiltBricks) {
            key.append(pos).append(",");
        }
        return key.toString();
    }

    private List<Stride> getAllPossibleStrides(WallState currState) {
        // Determine starting possible position for a stride
        int[] currPositions = currState.positionsBuiltBricks;
        int startRow = 0;
        int startCol = 0;

        for(int i = 0; i < currPositions.length; i++){
            if(currPositions[i] < currState.columns - 1) {
                startRow = i;
                startCol = currPositions[i] + 1;
                break;
            }
        }
        System.out.println("Starting row " + startRow + ", starting column " +  startCol);
        List<Stride> possibleStrides = new ArrayList<>();

        // We need to check 4 possible positions for moving the robot
        // Left, right, up and down
        // Generate all possible positions within stride range
        // There is some bug!!!
        for (int offsetX = -maxColumnsPerStride; offsetX <= maxColumnsPerStride; offsetX++){
            for(int offsetY = -maxRowsPerStride; offsetY <= maxRowsPerStride; offsetY++){
                int newStrideStartRow = offsetY + startRow;
                int newStrideStartCol = offsetX + startCol;

                Stride currStride = new Stride(newStrideStartRow, newStrideStartCol);
                if(isStridePossible(currStride, currState)){
                    possibleStrides.add(currStride);
                }
            }
        }

        return possibleStrides;
    }

    private boolean canPlaceBrick(int i, int j, int[] positionsBuiltBricks){
        //if it is the first row, always can place
        if(i == 0) return true;

        boolean startsWithHalf = wall.wallMatrix[i][0] == 1;
        if(!startsWithHalf) {
            if (j == columns - 1) return positionsBuiltBricks[i - 1] >= j;
            else return positionsBuiltBricks[i - 1] >= j + 1;
        }
        else return positionsBuiltBricks[i - 1] >= j;
    }

    public WallState buildBricksInRange(WallState currState, Stride stride) {
        int countBuildableBricks = 0;

        WallState newState = new WallState(
                currState.positionsBuiltBricks.clone(),
                new ArrayList<>(currState.stridesTaken),
                currState.rows,
                currState.columns
        );

        newState.stridesTaken.add(stride);

        int endRow = Math.min(stride.startRow + maxRowsPerStride, rows);
        int endCol = Math.min(stride.startCol + maxColumnsPerStride, columns);

        for (int i = stride.startRow; i < endRow; i++){
            for(int j = stride.startCol; j < endCol; j++){
                //If we are trying to build the first brick and it is a half brick, then we can reach 3 full and 1 half brick
                if(j == 0 && wall.wallMatrix[i][0] == 1)
                {
                    endCol++;
                    //System.out.println("Max brick range increased with one due to half brick at start");
                }
                if(canPlaceBrick(i, j, newState.positionsBuiltBricks))  {
                    //System.out.println("Building brick at position (" + i + ", " + j + ")");
                    countBuildableBricks++;
                    newState.positionsBuiltBricks[i] = j;
                }
            }
        }
        System.out.println("Build bricks: " + countBuildableBricks);
        return newState;
    }


    public int calculateNumberBricksInRange(int startX, int startY, int[] positionsBuiltBricks) {
        int countBuildableBricks = 0;

        int endY = Math.min(startY + maxRowsPerStride, rows);
        int endX = Math.min(startX + maxColumnsPerStride, columns);

        for (int i = startY; i < endY; i++) {
            for(int j = startX; j < endX; j++) {
                //If we are trying to build the first brick and it is a half brick, then we can reach 3 full and 1 half brick
                if(j == 0 && wall.wallMatrix[i][0] == 1) endX = Math.min(endX + 1, columns);
                if(canPlaceBrick(i, j, positionsBuiltBricks)) countBuildableBricks++;
            }
        }
        return countBuildableBricks;
    }
    // Checks wheather stride would get out of the boundaries of the wall and also if it is going to build new bricks
    private boolean isStridePossible(Stride stride, WallState currWallState){
        // A stride is not possible if we are at the last col/row
        if (stride.startCol < 0 || stride.startRow < 0 ||
                stride.startCol >= currWallState.columns ||
                stride.startRow >= currWallState.rows) {
            return false;
        }
        // Optimize it, so that it doesn't have to calculate every time all the bricks,
        // but the moment it finds one brick that can be built to continue
        return calculateNumberBricksInRange(stride.startCol, stride.startRow, currWallState.positionsBuiltBricks) > 0;
    }

}
