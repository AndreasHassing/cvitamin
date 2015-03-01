package dk.itu.cvitamin.model;

import javafx.geometry.BoundingBox;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipInputStream;

/**
 * This class is responsible I/O related tasks.
 * It is also responsible for reading and parsing
 * a list that contains lines representing map elements
 * such as roads, buildings, etc.
 * 
 * Created by Dennis Thinh Tan Nguyen 04-02-2015.
 */
class IOhandler {

    private File fileUsed;
    //Arraylists with ways
    private final ArrayList<Way> mainRoads = new ArrayList<>();
    private final ArrayList<Way> minorRoads = new ArrayList<>();
    private final ArrayList<Way> waterWays = new ArrayList<>();
    private final ArrayList<Way> buildings = new ArrayList<>();
    private final ArrayList<Way> coastLines = new ArrayList<>();
    private final ArrayList<Way> greenArea = new ArrayList<>();
    private final ArrayList<Way> greySurface = new ArrayList<>();

    private double minLon; 
    private double maxLat;

    private BoundingBox bbox;

    //File methods
    /**
     * This method will receive a file as parameter and store it in the global field.
     * This method is used when you locate and select a file with address entries
     *
     * @param selected file with address entries
     */
    public void setFile(File selected) {
        fileUsed = null;
        fileUsed = selected;

        if (fileUsed != null)
            System.out.println(fileUsed.getName() + " was selected");

        else
            System.out.println("File could not be selected");
    }

    /**
     * Reads an input file with file extension:
     * .osm - .zip
     * @param filename file path
     */
    public void readFile(File filename) {
        try {
            String filePath = filename.getCanonicalPath();
            long time = System.nanoTime();

            // if (filePath.endsWith(".txt")) parseTxT(filename); // NEED REFACT

            if (filePath.endsWith(".osm")) parseOSM(filename);
            else if (filePath.endsWith(".zip")) parseZIP(filename);
            else System.err.println("File not recognized");
            System.out.printf("Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns current file
     * @return file
     */
    public File getFile() {
        return fileUsed;
    }

    /**
     * Stores every arraylists of lines within an arraylist
     * 
     * index 0 = greySurface
     * index 1 = green area
     * index 2 = buildings
     * index 3 = waterWays
     * index 4 = coastlines
     * index 5 = MinorRoads
     * index 6 = MainRoads
     * 
     * REMEMBER TO UPDATE NUMBER OF ARRAY IN testGetLines() IF YOU ADD MORE ARRAYLISTS
     *
     * @return arraylist containing arraylists of lines
     */
    public ArrayList getLines() {
        ArrayList<ArrayList> map = new ArrayList<>();
        map.add(greySurface);
        map.add(greenArea);
        map.add(buildings);
        map.add(waterWays);
        map.add(coastLines);
        map.add(minorRoads);
        map.add(mainRoads);
        return map;
    }

    /**
     * Returns bounding box
     * @return bounding box
     */
    public BoundingBox getBBox() { return bbox; }

    /**
     * Returns the minimum longitude of a map
     * @return minimum longitude 
     */
    public double getMinLon() { return minLon; }

    /**
     * Returns the maximum latitude of a map
     * @return maximum latitude
     */
    public double getMaxLat() { return maxLat; }

    /**
     * Parse a zip file containing an OSM file
     * @param filename zip file
     */
    public void parseZIP(File filename) {
        fileUsed = filename;
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            zip.getNextEntry();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(new InputSource(zip));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse an OSM file
     * @param filename osm file
     */
    void parseOSM(File filename) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            System.out.println(filename.toURI().toString());
            reader.parse(filename.toURI().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }

    //OSM HANDLER - INNERCLASS
    /**
     * This nested inner class is responsible for reading and parsing
     * the right information within  a map file (XML file)
     */
    class OSMHandler extends DefaultHandler {
        //Fields
        final HashMap<Long, Coordinate> map = new HashMap<>();
        Way way;
        boolean isBuilding;
        boolean isWaterWay;
        boolean isCoastLine;
        boolean isMainRoad;
        boolean isMinorRoad;
        boolean isGreenArea;
        boolean isGreySurface;

        //methods
        /**
         * Whenever the parser encounters a ending "way" tag, add the created way object in 
         * a propriate arraylist (eg. builings, mainRoads ...)
         */
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("way")) {
                if (isBuilding)
                {
                    buildings.add(way);
                }
                else if(isWaterWay)
                {
                    waterWays.add(way);
                }
                else if(isCoastLine)
                {
                    coastLines.add(way);
                }
                else if (isMainRoad)
                {
                    mainRoads.add(way);
                }
                else if (isMinorRoad)
                {
                    minorRoads.add(way);
                }
                else if (isGreenArea)
                {
                    greenArea.add(way);
                }
                else if (isGreySurface)
                {
                    greySurface.add(way);
                }
            }
        }

        public void startElement(String uri, String localName,
                String qName, Attributes atts) {
            switch (qName) {
                case "node":
                    setCoords(atts); //save lat and long coords
                    break;

                case "nd":
                    setLine(atts); //Create a way object
                    break;

                case "way":
                    reset(); //Reset boolean statements
                    break;

                case "bounds":
                    setBounds(atts); //Creating and setting bounds
                    break;

                case "tag":
                    setTag(atts); //Compare tags
                    break;
            }
        }

        /**
         * This method will retrieve the latitude and longitude coords
         * of a node, within the XML file/OSM file and store them in a 
         * hashmap.
         * @param atts  lat and long attribute
         */
        private void setCoords(Attributes atts) {
            float lat = Float.parseFloat(atts.getValue("lat"));
            float lon = Float.parseFloat(atts.getValue("lon"));
            long id = Long.parseLong(atts.getValue("id"));

            Coordinate coord = new Coordinate(lon,lat);
            map.put(id, coord);
        }


        /**
         * Plotting every related coordinates to a line 
         * within a way-object. The coordinates are being retrieved from
         * a hashmap that contains latitudes and longitudes coordinates 
         * @param atts attributes
         */
        private void setLine(Attributes atts) {
            long id = Long.parseLong(atts.getValue("ref"));
            Coordinate coord = map.get(id);

            if (coord == null) return;

            if (way == null) //new way
            {
                way = new Way(coord.x,coord.y);
            }
            else //already within a way
            {
                way.addMoreCoords(coord.x,coord.y);
            }
        }

        /**
         * Resets every boolean statements to false whenever parser encounters a
         * new way tag. 
         */
        private void reset() {
            way = null;
            isBuilding = false;
            isWaterWay = false;
            isCoastLine = false;
            isMainRoad = false;
            isMinorRoad = false;
            isGreenArea = false;
            isGreySurface = false;
        }

        /**
         * This method retrieves the bounds info of a map
         * @param atts bound attributes
         */
        private void setBounds(Attributes atts) {
            double minlat =
                Double.parseDouble(atts.getValue("minlat"));
            double minlon =
                Double.parseDouble(atts.getValue("minlon"));
            double maxlat =
                Double.parseDouble(atts.getValue("maxlat"));
            double maxlon =
                Double.parseDouble(atts.getValue("maxlon"));

            minLon = minlon;
            maxLat = maxlat;
            //Defining a new bounding box
            bbox = new BoundingBox(minlon, minlat, maxlon - minlon, maxlat - minlat);
        }

        /**
         * This method will set a propriate boolean statements to true every time
         * the parser encounters a k tag and/or v tag that equals to a given string (eg. k = "buildings").

         * This is used to separate different way types (eg. buildings, roads, land areas)*
         *
         * @param atts k and v attributes
         */
        private void setTag(Attributes atts) {
            if (atts.getValue("k").equals("building"))
                isBuilding = true;

            if (atts.getValue("k").equals("waterway") || atts.getValue("k").equals("natural") && atts.getValue("v").equals("water"))
                isWaterWay = true;

            if (atts.getValue("k").equals("coastline"))
                isCoastLine = true;

            if (atts.getValue("k").equals("highway") && atts.getValue("v").equals("primary") ||atts.getValue("v").equals("trunk")||
                    atts.getValue("v").equals("secondary") || atts.getValue("v").equals("tertiary"))
                isMainRoad = true;

            if (atts.getValue("k").equals("highway") && !atts.getValue("v").equals("primary") && !atts.getValue("v").equals("trunk") &&
                    !atts.getValue("v").equals("secondary") && !atts.getValue("v").equals("tertiary"))
                isMinorRoad = true;


            if(atts.getValue("k").equals("leisure") && atts.getValue("v").equals("park")
                    || atts.getValue("v").equals("wood") || atts.getValue("v").equals("scrub") || atts.getValue("v").equals("grasslands"))
                isGreenArea = true;

            if(atts.getValue("k").equals("surface") &&  atts.getValue("v").equals("asphalt") || atts.getValue("k").equals("surface") &&  atts.getValue("v").equals("paving_stones") ||
                    atts.getValue("k").equals("surface") &&  atts.getValue("v").equals("cobblestone") || atts.getValue("k").equals("surface") &&  atts.getValue("v").equals("paved"))
                isGreySurface = true;
        }
    }
}

