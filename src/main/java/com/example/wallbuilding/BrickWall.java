package com.example.wallbuilding;

public class BrickWall {
    public final double headJoint = 10;
    public final double bedJoint = 12.5;

    public int wallWidth;
    public int wallHeight;

    public Brick fullBrick;
    public Brick halfBrick;
    public int[][] wallMatrix;
    public int rows;
    public int columns;
    public double courseLength;
    public double courseHeight;

    public BrickWall(int width, int height) {
        this.wallWidth = width;
        this.wallHeight = height;
        this.fullBrick = new Brick(210, 50, 100);
        this.halfBrick = new Brick(100, 50, 100);
        this.rows = calculateNumberRows(wallHeight, fullBrick.calculateCourseHeight(bedJoint));
        this.columns = calculateNumberColumns(wallWidth, fullBrick.calculateCourseLength(headJoint)) + 1;
        this.courseHeight = fullBrick.calculateCourseHeight(bedJoint);
        this.courseLength = fullBrick.calculateCourseLength(headJoint);
        this.wallMatrix = createWallMatrixDesign();
    }


    public int calculateNumberRows(double height, double courseHeight){
        System.out.println("Stride height " + height + " courseHeight " + courseHeight);
        return (int) (height / courseHeight);
    }

    // This method would return the number of full bricks
    // Currently there is an assumption that the first row always starts with half brick
    // => the number of columns will be numFullBricks + 1
    public int calculateNumberColumns(double width, double courseLength) {
        int numFullBricks = 10;
        if (width % courseLength == 0) {
            System.out.println("Length is divisible, we can have a row with full bricks. IMPLEMENT!!!");
        } else {
            //(int) -> rounds down => we will need numFullBricks + 1 halfBrick
            System.out.println("Starting/Ending with a half brick");
            numFullBricks = (int) (width / courseLength);
        }
        System.out.println("Stride width " + width + " course Length " + courseLength);
        return numFullBricks;
    }

    private int[][] createWallMatrixDesign() {
        //1's represent the halfBricks
        //0's represent the fullBricks

        System.out.println("Rows: " + rows);
        System.out.println("Num Full Bricks: " + columns);

        int[][] wallMatrix = new int[rows][columns];

        for (int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(i % 2 == 0 && j == 0){
                    wallMatrix[i][j] = 1;
                }
                else if (i % 2 != 0 && j == columns - 1){
                    wallMatrix[i][j] = 1;
                }
                else wallMatrix[i][j] = 0;
            }
        }

        // Print the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(wallMatrix[i][j] + " ");
            }
            System.out.println();
        }
        return wallMatrix;
    }

}
