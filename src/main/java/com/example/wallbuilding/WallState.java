package com.example.wallbuilding;

import java.util.ArrayList;
import java.util.List;

public class WallState {

    int rows;
    int columns;
    int[] positionsBuiltBricks;
    List<Stride> stridesTaken;

    public WallState(int[] positions, List<Stride> strides, int rows, int columns) {
        this.positionsBuiltBricks = positions.clone();
        this.stridesTaken = new ArrayList<>(strides);
        this.rows = rows;
        this.columns = columns;
    }
}
