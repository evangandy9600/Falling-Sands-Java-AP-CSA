import java.awt.*;
import java.util.*;
import java.lang.Thread;

public class SandLab
{
    public static void main(String[] args)
    {
        SandLab lab = new SandLab(120, 90);
        lab.run();
    }

    //add constants for particle types here
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int ACID = 4;
    public static final int GAS = 5;
    public static final int SANDCIRCLE = 6;
    public static final int WATERCIRCLE = 7;
    public static final int ACIDCIRCLE = 8;
    public static final int DEATHRAY = 9;
    public static final int RESET = 10;

    // Color Adjust
    // public static final int 20;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols) {
        grid = new int[numRows][numCols];
        String[] names;
        names = new String[11];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[ACID] = "Acid";
        names[GAS] = "Gas";
        names[SANDCIRCLE] = "Bucket of Sand";
        names[WATERCIRCLE] = "Bucket of Water";
        names[ACIDCIRCLE] = "Bucket of Acid";
        names[DEATHRAY] = "Ray of Death";
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

        else if(tool == SANDCIRCLE) {
            createCircle(row, col, 4, SAND);
        }

        else if(tool == WATERCIRCLE) {
            createCircle(row, col, 4, WATER);
        }

        else if(tool == ACIDCIRCLE) {
            createCircle(row, col, 4, ACID);
        }

        else if(tool == DEATHRAY) {
            for(int r = 0; r < grid.length; r++) {
                for(int c = col; c < grid[0].length && c < col + 3; c++) {
                    grid[r][c] = DEATHRAY;
                }
            }
        }

        grid[row][col] = tool;
        System.out.println("Location Clicked: " + col + ", " + row);
    }

    public void createCircle(int row, int col, int radius, int toolType) {
        for (int i = row; i < 2 * radius + row && i < grid.length; i++) {
            for (int j = col; j < 2 * radius + col && j < grid[0].length; j++) {
                // Calculate the distance from the center of the circle
                int xDist = i - row - radius;
                int yDist = j - col - radius;
                double distance = Math.sqrt(xDist*xDist + yDist*yDist);

                // If the distance is less than or equal to the radius, set the value in the array to 1
                if (distance <= radius) {
                    grid[i][j] = toolType;
                }
            }
        }
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

                // water depth
                double x = Math.random();
                if(grid[r][c] == WATER && r - 1 > -1 && grid[r - 1][c] == EMPTY && x < 0.3)
                    display.setColor(r, c, new Color(255,255,255));
                else if(grid[r][c] == WATER && r - 2 > -1 && grid[r - 2][c] == EMPTY && x < 0.3)
                    display.setColor(r - 1, c, new Color(25,100,255));
                else if(grid[r][c] == WATER && r - 3 > -1 && grid[r - 3][c] == EMPTY && x < 0.3)
                    display.setColor(r - 2, c, new Color(15,50,255));
                else if(grid[r][c] == WATER && x < 0.01)
                    display.setColor(r, c, new Color(0,150,255));
                else if(grid[r][c] == WATER)
                    display.setColor(r, c, new Color(0,0,255));

                if(grid[r][c] == ACID)
                    display.setColor(r, c, new Color(0,255,0));
                if(grid[r][c] == GAS)
                    display.setColor(r, c, new Color(188,198,204));
                if(grid[r][c] == DEATHRAY)
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

        if(toolType == DEATHRAY && Math.random() < 0.7) {
            grid[x][y] = DEATHRAY;
        }

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

    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } 
        
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
