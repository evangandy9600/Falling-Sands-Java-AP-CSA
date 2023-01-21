import java.awt.*;
import java.nio.file.WatchEvent;
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

    // Color Adjust
    // public static final int 20;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols) {
        grid = new int[numRows][numCols];
        String[] names;
        names = new String[6];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[ACID] = "Acid";
        names[GAS] = "Gas";

        display = new SandDisplay("Falling Sand", numRows, numCols, names);
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool) {
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
	        }
	    }
    }

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step() {

		int x = (int) (Math.random() * grid.length -1);
		int y = (int) (Math.random() * grid[0].length -1);
        
        /*
        if (grid[x][y] == SAND) {
			if(grid[x + 1][y] == EMPTY || grid[x + 1][y] == WATER) {
				grid[x][y] = EMPTY;
				grid[x + 1][y] = SAND;
			}
		}
        */

        if(grid[x][y] == WATER) {
			fluid(x, y, 1, WATER);
        }

        else if(grid[x][y] == GAS) {
            fluid(x, y, -1, GAS);
        }

        else if(grid[x][y] == ACID) {
            fluid(x, y, 1, ACID);
        }

        else if(grid[x][y] == SAND) {
            fluid(x, y, 1, SAND);
        }
        
    }

    public void fluid(int x, int y, int gravity, int toolType) {
		int num = (int) (Math.random() * 4);

        /*
         * conditions
         * create random number (left, right, gravity)
         * 
         * if (left && y - 1 > - 1 && grid[x][y - 1] == EMPTY);
         * else if (right && y + 1 > grid[0].length && grid[x][y + 1] == EMPTY);
         * else if (x + gravity > -1 && x + gravity < grid.length && grid[x + gravity] == EMPTY);
         * else keep current pos
         */

        grid[x][y] = EMPTY;

        if(toolType == WATER) {
            // left 25%
            if (num == 0 && y - 1 > - 1 && grid[x][y - 1] == EMPTY){
                grid[x][y - 1] = toolType;
            }

            // right 25%
            else if (num == 1 && y + 1 < grid[0].length && grid[x][y + 1] == EMPTY) {
                grid[x][y + 1] = toolType;

            }

            // gravity 50%
            else if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY)) {
                grid[x + gravity][y] = toolType;
            }
            
            // stay
            else {
                grid[x][y] = toolType;
            }
        }

        if(toolType == GAS) {
            // left 25%
            if (num == 0 && y - 1 > - 1 && grid[x][y - 1] == EMPTY){
                grid[x][y - 1] = toolType;
            }

            // right 25%
            else if (num == 1 && y + 1 < grid[0].length && grid[x][y + 1] == EMPTY) {
                grid[x][y + 1] = toolType;

            }

            // gravity 50%
            else if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY || grid[x + gravity][y] == WATER)) {
                grid[x + gravity][y] = toolType;

                if(grid[x + gravity][y] ==  WATER) {
                    grid[x][y] = WATER;
                }

            }
            
            // stay
            else {
                grid[x][y] = toolType;
            }
        }

        if(toolType == SAND) {

            // gravity 50%
            if (x + gravity > -1 && x + gravity < grid.length && (grid[x + gravity][y] == EMPTY || grid[x + gravity][y] == WATER)) {
                if(grid[x + gravity][y] == WATER) {
                    grid[x][y] = WATER;
                }

                grid[x + gravity][y] = toolType;
            }

            else {
                grid[x][y] = toolType;
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
