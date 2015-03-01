package dk.itu.cvitamin;

import Model.PrimaryModel;
import javafx.geometry.BoundingBox;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;

public class PrimaryModelTest {

    private PrimaryModel model;
    private int NumbOfBuilding = 1;
    private  int NumbOfRoad = 1;

    /**
     * Creates a new model object before each test
     */
    @Before
    public void setUp() {
        model = new PrimaryModel();

    }

    /**
     * Clears model after each test (This might not be necessary as we create a new model each time?) 
     */
    @After
    public void tearDown() {
        model = null;
    }

    /**
     * Test to make sure that a OSM file is being read and loaded
     */
    @Test
    public void testReadOSMFile() {
        File filePath = new File("src/Tests/testMap.osm");
        model.readFile(filePath);
        Assert.assertNotNull("The arraylist with lines is null. Which means the arrayList failed to be created", model.getLines());

        //Test OSMParser
        Assert.assertFalse("OSMparser did not parse 'way' objects to any sub-arrayLists or the sub-arraylists was not added to the main array list "
                ,model.getLines().isEmpty());
    }

    /**
     * Test to make sure that a Zip file is being read and loaded 
     */
    @Test
    public void testReadZipFile() throws Exception {
        File filePath = new File("src/Tests/testMap.zip");
        model.readFile(filePath);
        Assert.assertNotNull("The arraylist with lines is null. Which means the arrayList failed to be created", model.getLines());

        //Test OSMParser
        Assert.assertFalse("OSMparser did not parse 'way' objects to any sub-arrayLists or the sub-arraylists was not added to the main array list "
                ,model.getLines().isEmpty());
    }

    /**
     * Test to make sure that a Bin file is being read and loaded 
     */
    @Test
    public void testReadBinFile() throws Exception {

    }

    /**
     * Test to make sure that our model / loaded map can be saved as a bin file
     */
    @Test
    public void testSaveBinFile() throws Exception {

    }

    /**
     * Test to make sure that the last loaded file location is being stored and returned.
     * This is used whenever we want to load a new map and that start directory is equal to last loaded file location  
     */
    @Test
    public void testGetLastUsed() throws Exception {
        File filePath = new File("src/Tests/testMap.osm");
        model.readFile(filePath);
        Assert.assertNotNull("Last used file returned null. Does the file get loaded?",model.getLastUsed());
    }

    /**
     * Test to make sure that map data is being read from OSM file and stored in propriate arrayLists 
     */
    @Test
    public void testGetLines() throws Exception {
        int numberOfArray = 7;

        File filePath = new File("src/Tests/testMap.osm");
        model.readFile(filePath);
        ArrayList lines = model.getLines();
        Assert.assertNotNull("No ArrayList was created. Check IOhandler class",lines);
        Assert.assertEquals("Returned wrong size. Check IOHandler",numberOfArray, lines.size());

        for(int i = 0 ; i < lines.size(); i++) {
            ArrayList ways = (ArrayList) lines.get(i);
            if (i == 2) {
                Assert.assertEquals("The returned size of building list should be 1, but " + ways.size() + " was returned",
                NumbOfBuilding, ways.size()); //Testing buildings
            } else if (i==5) {
                Assert.assertEquals("The returned size of road list should be 1, but " + ways.size() + " was returned",
                NumbOfRoad, ways.size()); //Testing minor roads
            } else {
                Assert.assertEquals("List in index"+i+" Should be empty but "+ways.size()+" was returned. Check IOHandler class and Innerclass in IOHandler"
                        ,0,ways.size());
            }
        }
    }

    /**
     * Test to make sure that bounding box is retrieved and its information are correct
     */
    @Test
    public void testGetBoundingBox() throws Exception {
        File filePath = new File("src/Tests/testMap.osm");
        model.readFile(filePath);
        Assert.assertNotNull("No bounding box was returned",model.getBoundingBox());

        BoundingBox bbbox = model.getBoundingBox();

        Assert.assertEquals("minLon should be 12.6035600, but "+bbbox.getMinX()+" was returned", //Getting minLon as validation -- 12.6035600 is the true value
                12.6035600,bbbox.getMinX(),
                0.0001); //fuzz factor," since doubles may not be exactly equal.
    }

    /**
     * Test to make sure that loaded arrayList with map data are being cleared
     */
    @Test
    public void testClearList() throws Exception {
        File filePath = new File("src/Tests/testMap.zip");
        model.readFile(filePath);
        Assert.assertNotNull("The arraylist with lines is null. Which means the arrayList failed to be created", model.getLines());
        model.clearList();
        Assert.assertEquals("Failed to clear line list. Check clearList() in primary model",0,model.getLines().size()); //Clear list should empty the arraylist
    }
}

