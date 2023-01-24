import java.awt.*;
import java.util.*;

public class SandLab
{
    public static void main(String[] args)
    {
        SandLab lab = new SandLab(60, 120);
        lab.run();
    }

    //add constants for particle types here
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int ACID = 4;
    public static final int GAS = 5;
    public static final int CIRCLE = 6;
    public static final int RESET = 7;

    // Color Adjust
    // public static final int 20;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols) {
        grid = new int[numRows][numCols];
        String[] names;
        names = new String[8];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[ACID] = "Acid";
        names[GAS] = "Vapor";
        names[CIRCLE] = "Sand Circle";
        names[RESET] = "Reset";

        display = new SandDisplay("Falling Sand", numRows, numCols, names);
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool) {

        if(tool == RESET) {
            for(int r = 0; r < grid.length; r++) {
                for(int c = 0; c < grid[r].length; c++) {
                    grid[r][c] = RESET;
                }
            }
        }

        if(tool == CIRCLE) {
        int radius = 8;
        int startX = row;
        int startY = col;
        // circle = new int[2*radius][2*radius];

        // Iterate over the rows and columns of the array
        for (int i = startX; i < 2*radius+startX; i++) {
            for (int j = startY; j < 2*radius+startY; j++) {
                // Calculate the distance from the center of the circle
                int xDist = i - startX - radius;
                int yDist = j - startY - radius;
                double distance = Math.sqrt(xDist*xDist + yDist*yDist);

                // If the distance is less than or equal to the radius, set the value in the array to 1
                if (distance <= radius) {
                    grid[i][j] = SAND;
                }
            }
        }
        }

        grid[row][col] = tool;
        System.out.println("Location Clicked: " + col + ", " + row);
        // display.updateDisplay();
    }

    // copies each element of grid into the display
    public void updateDisplay() {
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[r].length; c++){
                if (grid[r][c] == METAL)
                    display.setColor(r, c, new Color(120,120,120));
                if(grid[r][c] == EMPTY)
                    display.setColor(r, c, new Color(0,0,0));
                if(grid[r][c] == SAND)
                    display.setColor(r, c, new Color(255,255,0));
                if(grid[r][c] == WATER)
                    display.setColor(r, c, new Color(0,0,255));
                if(grid[r][c] == ACID)
                    display.setColor(r, c, new Color(0,255,0));
                if(grid[r][c] == GAS)
                    display.setColor(r, c, new Color(188,198,204));
                if(grid[r][c] == CIRCLE)
                    display.setColor(r, c, new Color(255,0,0));
	        }
	    }
    }

    //causes one random particle to maybe do something.
    public void step() {

		int x = (int) (Math.random() * grid.length);
		int y = (int) (Math.random() * grid[0].length);

        int num = (int) (Math.random() * 3);
        int toolType = grid[x][y];
        grid[x][y] = EMPTY;

        if(toolType == EMPTY) {
            grid[x][y] = EMPTY;
        }

        else if(toolType == METAL) {
            grid[x][y] = METAL;
        }
        
        else if(toolType == SAND) {
            int gravity = 1;

            // gravity 100%
            if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY || grid[x + gravity][y] == WATER || grid[x + gravity][y] == ACID || grid[x + gravity][y] == GAS)) {
                if(grid[x + gravity][y] == WATER) {
                    grid[x][y] = WATER;
                }

                else if(grid[x + gravity][y] == ACID) {
                    grid[x][y] = ACID;
                }

                else if(grid[x + gravity][y] == GAS) {
                    grid[x][y] = GAS;
                }

                grid[x + gravity][y] = SAND;
            }

            else {
                grid[x][y] = SAND;
            }
        }

        else if(toolType == WATER) {
            int gravity = 1;
            // left 25%
            if (num == 0 && y - 1 > - 1 && grid[x][y - 1] == EMPTY){
                grid[x][y - 1] = WATER;
            }

            // right 25%
            else if (num == 1 && y + 1 < grid[0].length && grid[x][y + 1] == EMPTY) {
                grid[x][y + 1] = WATER;

            }

            // gravity 50%
            else if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY)) {
                grid[x + gravity][y] = WATER;
            }
            
            // stay
            else {
                grid[x][y] = WATER;
            }
        }

        else if(toolType == GAS) {
            int gravity = -1;
            // left 25%
            if (num == 0 && y - 1 > - 1 && grid[x][y - 1] == EMPTY){
                grid[x][y - 1] = GAS;
            }

            // right 25%
            else if (num == 1 && y + 1 < grid[0].length && grid[x][y + 1] == EMPTY) {
                grid[x][y + 1] = GAS;

            }

            // gravity 50%
            else if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY || grid[x + gravity][y] == WATER || grid[x + gravity][y] == ACID)) {
                grid[x + gravity][y] = GAS;

                if(grid[x + gravity][y] ==  WATER) {
                    grid[x][y] = WATER;
                }

                else if(grid[x + gravity][y] == ACID) {
                    grid[x][y] = ACID;
                }

            }
            
            // stay
            else {
                grid[x][y] = GAS;
            }
        }

        else if(toolType == ACID) {
            int gravity = 1;
            // left 25%
            if (num == 0 && y - 1 > - 1 && (grid[x][y - 1] == EMPTY || grid[x][y - 1] == METAL)){
                grid[x][y - 1] = ACID;
            }

            // right 25%
            else if (num == 1 && y + 1 < grid[0].length && (grid[x][y + 1] == EMPTY || grid[x][y + 1] == METAL)) {
                grid[x][y + 1] = ACID;
            }

            // gravity 50%
            else if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY || grid[x + gravity][y] == METAL)) {
                grid[x + gravity][y] = ACID;
            }
            
            // stay
            else {
                grid[x][y] = ACID;
            }
        }
    }

    // public boolean isEmpty
    //do not modify
    public void run() {
        while (true) {
            
            for (int i = 0; i < display.getSpeed(); i++) {
                step();
            }

            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse

            int[] mouseLoc = display.getMouseLocation();

            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}
