package cityrescue;

import cityrescue.exceptions.*;
    /**
     * to handle all the logic of the map based things 
     * mostly flags
     * coordinates 
     */
public class CityMap {

    private int width;
    private int height;
    private boolean[][] blocked;

    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public boolean inBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    /**
     * set obstacle blocked
     * checks its not out of bounds
     * set it to the value of true or false  
     */
    public void setBlocked(int x, int y, boolean value)
            throws InvalidLocationException {

        if (!inBounds(x, y)) {
            throw new InvalidLocationException("Out of bounds");
        }

        blocked[x][y] = value;
    }
    /**
     * add obstacle
     * checks its not out of bounds
     * set it to true 
     */
    public void addObstacle(int x, int y)
            throws InvalidLocationException {

        if (!inBounds(x, y)) {
            throw new InvalidLocationException("Out of bounds");
        }

        blocked[x][y] = true;
    }
    /**
     * remove obstacle
     * checks its not out of bounds
     * set it to false 
     */

    public void removeObstacle(int x, int y)
            throws InvalidLocationException {

        if (!inBounds(x, y)) {
            throw new InvalidLocationException("Out of bounds");
        }

        blocked[x][y] = false;
    }
}
