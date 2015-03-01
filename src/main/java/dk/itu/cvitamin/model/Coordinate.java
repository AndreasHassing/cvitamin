package dk.itu.cvitamin.model;

/**
 * This class creates an instance containing a single set of coordinates  
 * Created by Dennis Thinh Tan Nguyen 17-02-2015.
 */
public class Coordinate {
    final double x;
    final double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Getters
    /**
     * Retrieves the x coordinate 
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Retrieves the y coordinate 
     * @return y coordinate
     */
    public double getY() {
        return y;
    }

}
