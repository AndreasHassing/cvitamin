package dk.itu.cvitamin.view;

import dk.itu.cvitamin.model.Coordinate;
import dk.itu.cvitamin.model.Way;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.action.Action;
//import org.controlsfx.dialog.Dialog;
//import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Dennis Thinh Tan Nguyen 04-02-2015.
 */
public class PrimaryView extends Application {
    //View inits
    private static boolean launched = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("PrimaryView.fxml"));
        primaryStage.setResizable(true);
        primaryStage.setTitle("Line drawer X PRO");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void launchApp(String[] args) {
        if (!launched) {
            launch(args);
            launched = true;
        }
        else System.out.println("Already running");
    }

    //Fields
    private GraphicsContext gc;
    private Canvas canvas;
    private double xScale,yScale;

    private double minLon,maxLat;

    private BoundingBox boundingBox;

    private Affine affine = new Affine();
    private static boolean isToggleColour;
    private Color roadStroke;
    private Color buildingFill;

    //Methods
    public PrimaryView() {

    }

    /**
     * Draws image with lines.
     * The coordinates are retrieved from LineObjects
     *
     * We use "n" to define index: 
     * index 0 = grey Surface
     * index 1 = green area 
     * index 2 = buildings
     * index 3 = waterWays
     * index 4 = coastlines
     * index 5 = MinorRoads
     * index 6 = MainRoads
     */
    public void draw(ArrayList<ArrayList> lines) {
        clearCanvas();
        gc.setLineCap(StrokeLineCap.BUTT);    //Uses butt cap to stroke lines

        int n = 0; //Type of line
        for(ArrayList type : lines) {
            if (!type.isEmpty()) {
                //noinspection unchecked
                drawLines(type, n);
            }
            n++;
        }
    }

    /**
     * This method will iterate over an arraylist of way-objects and 
     * call the right draw method (eg. drawBuildings...)
     * 
     * index 0 = grey Surface
     * index 1 = green area 
     * index 2 = buildings
     * index 3 = waterWays
     * index 4 = coastlines
     * index 5 = MinorRoads
     * index 6 = MainRoads* 
     *  
     * @param lines Arraylist with way objects
     */
    private void drawLines(ArrayList<Way>lines, int type) {
        for (Way road : lines)
        {
            ArrayList<Coordinate> coords = road.getWay(); //Retrieving coords

            gc.beginPath();
            for (Coordinate coord : coords)
            {
                gc.lineTo(coord.getX(), coord.getY());
            }

            //Switch between line type
            switch (type) {
                case 0:
                    drawGreySurface();
                    break;
                case 1:
                    drawGreenArea();
                    break;
                case 2:
                    drawBuilding();
                    break;
                case 3:
                    drawWaterway();
                    break;
                case 4:
                    drawCoastLine();
                    break;
                case 5 :
                    drawMinorRoad();
                    break;
                case 6 :
                    drawMainRoad();
                    break;
            }
            gc.closePath();
        }
    }

    /**
     * Draw main roads
     */
    private void drawMainRoad() {
        if (isToggleColour) gc.setStroke(roadStroke);
        else gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.00004);
        gc.stroke();
    }

    /**
     * Draw minor roads 
     */
    private void drawMinorRoad() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.00002);
        gc.stroke();
    }

    /**
     * Draw buildings
     */
    private void drawBuilding() {
        gc.setLineWidth(0.00001);
        if (isToggleColour) gc.setFill(buildingFill);
        else gc.setFill(Color.GREY);
        gc.setStroke(Color.BLACK);
        gc.fill();
        gc.stroke();
    }

    /**
     * Draw and fill water related elements 
     */
    private void drawWaterway() {
        if (isToggleColour) gc.setFill(Color.LIGHTBLUE);
        else gc.setFill(Color.LIGHTBLUE);
        gc.setStroke(Color.BLACK);
        gc.fill();
        gc.stroke();
    }

    /**
     * Draw coast line 
     */
    private void drawCoastLine() {
        gc.setFill(Color.BLACK);
        gc.fill();
        gc.stroke();
    }

    /**
     * Draw everything related to green elements. Eg: Parks, forests 
     */
    private void drawGreenArea() {
        gc.setLineWidth(0.00001);
        if (isToggleColour) gc.setFill(Color.LIGHTGREEN);
        else gc.setFill(Color.LIGHTGRAY);
        gc.fill();
        gc.stroke();
    }

    /**
     * Draw everything related to grey elements. Eg: Asphalt or cobblestone 
     */
    void drawGreySurface() {
        gc.setFill(Color.LIGHTGRAY);
        gc.fill();
    }

    /**
     * Change color of fill and stroke
     * @param fill color
     * @param stroke color
     * @param lines map data
     */
    public void ChangeColor(Color fill, Color stroke, ArrayList<ArrayList> lines) {
        gc.setFill(fill);
        roadStroke = stroke;
        buildingFill = fill;
        draw(lines);
    }

    /**
     * Toggles the colour on or off 
     */
    public void toggleColor(ArrayList<ArrayList> lines) {
        isToggleColour = !isToggleColour;
        draw(lines);
    }


    /**
     * Clears the canvas
     */
    public void clearCanvas() {
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Pan the map 
     * @param xTranslate x translation
     * @param yTranslate y translation
     * @param lines arrayList with map data
     */
    public void pan(double xTranslate, double yTranslate, ArrayList<ArrayList> lines) {
        /*
           affine.appendTranslation(xTranslate,yTranslate);
           clearCanvas();
           draw(lines); //Dynamic change
           */
    }

    /**
     * Zoom in by increasing scale
     * @param lines ArrayList with map data
     */
    public void zoom(double factor, ArrayList<ArrayList> lines) {
        dialogMessages("info", "Zooming is currently not supported");
        /*
           double xfactor = factor;
           double yfactor = factor;
           xScale *= xfactor;
           yScale *= yfactor;
           gc.scale(xScale,yScale);
           draw(lines); //Dynamic change
           */
    }

    /**
     * Universal method to display a dialog box with a message
     * It can either be an info type or error type. We are using controlFX java libraries to show
     * the dialog boxes
     * credits: http://fxexperience.com/controlsfx/
     *
     * @param type ("error" // "info" // "confirmation")
     * @param m message to display.
     */
    @SuppressWarnings("deprecation")
    public boolean dialogMessages(@SuppressWarnings("SameParameterValue") String type, String m) {
        Alert alert = null;
        switch (type) {
            case "error":  //noinspection deprecation
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText(m);
                alert.showAndWait();
                break;

            case "info":
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText(m);
                alert.showAndWait();
                break;

            case "confirmation":
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText(m);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) return true;
        }
        return false;
    }

    /**
     * Method to locate an input file. A fileChooser dialog box will be shown
     * to the user, so he can locate a specific input file
     * @param lastUsedFile last used file location
     */
    public File locateFile(File lastUsedFile) {
        FileChooser fileChooser = new FileChooser();

        //Open directory from existing directory
        if(lastUsedFile != null)
        {
            File existDirectory = lastUsedFile.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        }

        //Setting extension filters.
        String[] ext = new String[]{"*.osm","*.zip"};
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("zip & osm files", ext);
        fileChooser.getExtensionFilters().add(filter);

        //Display dialog
        fileChooser.setTitle("Select map file");

        return fileChooser.showOpenDialog(new Stage()); //returns selected file from pop-up window
    }

    /**
     * Reset transform variables of map
     * This is usually used when loading a new map
     */
    public void resetTransform() {
        xScale = canvas.getWidth()*0.45 / boundingBox.getWidth();
        yScale = canvas.getHeight() / boundingBox.getWidth();

        System.out.println(xScale);
        System.out.println(yScale);

        affine = new Affine();
        affine.appendScale(xScale*2, -yScale*2);
        affine.appendTranslation(-minLon, -maxLat);
        gc.setTransform(affine);
    }

    /**
     * retrieve and set Graphics context parsed
     * by controller.
     * @param gcArg - Graphics Context
     */
    public void setGC(GraphicsContext gcArg) {
        gc = gcArg;
    }

    /**
     * Set canvas parsed by controller
     * @param canvas parsed from controller
     */
    public void setCanvas(Canvas canvas) { this.canvas = canvas; }

    /**
     * sets the minimum longitude and maximum latitude.
     * This is used to place the map correctly on the canvas  
     * @param lon longitude
     * @param lat latitude
     */
    public void setLonLat(double lon, double lat) {
        minLon = lon;
        maxLat = lat;
    }

    /**
     * Set bounding box
     * @param boundingBox Bounding box
     */
    public void setBBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}

