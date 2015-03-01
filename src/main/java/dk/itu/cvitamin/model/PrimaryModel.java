package dk.itu.cvitamin.model;

import javafx.geometry.BoundingBox;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is the primary model that contains and
 * handle raw map data.
 * Created by Dennis Thinh Tan Nguyen 04-02-2015.
 */
public class PrimaryModel {

    private final IOhandler iOhandler;
    private ArrayList<ArrayList> lines;

    private double minLon;
    private double maxLat;

    /**
     * Constructor
     */
    public PrimaryModel() {
	iOhandler = new IOhandler();
	lines = new ArrayList<>();
    }

    /**
     * This method parses a map file to IO-handler object and
     * retrieves the processed data from it.
     */
    public void readFile(File filename) {
	iOhandler.setFile(filename); //setting file
	iOhandler.readFile(filename);

	//noinspection unchecked
	lines = iOhandler.getLines();
	maxLat = iOhandler.getMaxLat();
	minLon = iOhandler.getMinLon();
    }

    /**
     * Retrieve last used file. It's location will be used when you want
     * to load another map
     * @return last used file
     */
    public File getLastUsed() {
	return iOhandler.getFile();
    }

    /**
     * Retrieves an arraylist of road cordiantes
     * index 0 = greySurface
     * index 1 = green area
     * index 2 = buildings
     * index 3 = waterWays
     * index 4 = coastlines
     * index 5 = MinorRoads
     * index 6 = MainRoads
     * @return arraylist containing arraylists of lines
     */
    public ArrayList<ArrayList> getLines() { return lines; }

    /**
     * Returns the boundingBox
     * @return boundingBox
     */
    public BoundingBox getBoundingBox() { return iOhandler.getBBox(); }

    /**
     * Returns the minimum longitude of a map
     * @return minimum longitude
     */
    public double getMinLon() {
	return minLon;
    }

    /**
     * Returns the maximum latitude of a map
     * @return maximum latitude
     */
    public double getMaxLat() {
	return maxLat;
    }

    /**
     * Purge stored arraylist with map data
     */
    public void clearList()
    {
	lines.clear();
    }
}

