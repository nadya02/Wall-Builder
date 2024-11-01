# Brick Wall Building Project

## Setup and Running

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the main class:
   ```bash
   java com.example.wallbuilding.Main
   ```

### Usage Instructions
1. Launch the application
2. Enter wall dimensions in the initial window and click "Build Wall"
4. Press Enter key to see each stride colored sequentially
   - Each stride will be colored in a different random color
   (The building brick by brick is not implemented)
  - It might require clicking on the pane before pressing ENTER

## Wall Building Algorithm

### Key Components
- Uses Breadth-First Search (BFS) to find optimal stride sequence(Definetly not the fastest approach, but the easiest to implement for now I guess)

## Issues with the code
- There is a bug with the algorithm, so that it always thinks it should color up-left corner first, followed by up-right, down-left and down-right, which leaves the middle section not fully colored.
- Currently it also doesn't work if the wall width is divisible by the fullBrickLength(because then the first row will be starting with full bricks)
- Also the brick by brick coloring is not implemented yet

## Project Structure

### Main Classes
1. **Main.java**
   - Initializes JavaFX application
   - Loads the initial start page

2. **BrickWall.java**
   - Contains wall configuration and properties
   - Manages wall matrix creation

3. **BrickAlgorithm.java**
   - Implements the wall-building algorithm using BFS

4. **Controllers:**
   - **StartPageController.java**: Handles initial input of wall dimensions
   - **BrickWallPageController.java**: Manages wall visualization and stride coloring

5. **Brick.java**
   - Represents a brick and it's properties

6. **Stride.java**
   - Represents robot movement positions
