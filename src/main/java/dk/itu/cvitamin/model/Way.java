package dk.itu.cvitamin.model;

import java.util.ArrayList;

/**
 * This class creates an instance containing an ArrayList with coordinates
 * The instance is a representation of a path with n numbers of coordinates 
 * Created by Dennis Thinh Tan Nguyen 07-02-2015.
 */
public class Way {
   private final ArrayList<Coordinate> coordinates;

   public Way(double x, double y)
   {
      coordinates = new ArrayList<>();
      coordinates.add(new Coordinate(x,y));
   }

   /**
    * Add more coordinates to the object
    * @param x coordinate
    * @param y coordinate
    */
   public void addMoreCoords(double x, double y)
   {
      coordinates.add(new Coordinate(x,y)); 
   }

   /**
    * Returns the arraylist that contains the coordinates
    * @return Arraylist with coordinates
    */
   public ArrayList<Coordinate> getWay()
   {
      return coordinates;
   }
}
