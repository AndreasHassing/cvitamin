package dk.itu.cvitamin.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import dk.itu.cvitamin.model.PrimaryModel;
import dk.itu.cvitamin.view.PrimaryView;

import java.io.File;

public class PrimaryStageController {

    //Fields
    @FXML
    private ColorPicker colourPickerFill;

    @FXML
    private ColorPicker colourPickerStroke;

    @FXML
    private Canvas canvas;

    private boolean isfileLoaded;

    private static double pressedX, pressedY; //Mouse coords
    private static double releasedX, releasedY; //Mouse coords

    private static double xTranslate, yTranslate;

    private final PrimaryModel model;
    private final PrimaryView view;



    //Constructor
    public PrimaryStageController() {
        model = new PrimaryModel();
        view = new PrimaryView();
        isfileLoaded = false;
    }

    //initializer
    @FXML
    private void initialize() {
        view.setGC(canvas.getGraphicsContext2D());
        view.setCanvas((canvas));
        view.clearCanvas();

    }

    //Methods
    /**
     * Retrieves the color value of colorPicker and parse it to View;
     */
    @FXML
    private void changeColor() {
        view.ChangeColor(colourPickerFill.getValue(), colourPickerStroke.getValue(), model.getLines());
    }


    /**
     * Method used to change status of color being enabled or disabled
     */
    @FXML
    private void toggleColour() {
        view.toggleColor(model.getLines());
    }

    /**
     * Clears the canvas and purge stored map data
     */
    @FXML
    private void clear() {
        isfileLoaded = false;
        view.clearCanvas();
        model.clearList();
    }


    /**
     * Zoom function that use the mouse wheel to zoom.
     * @param event scrollwheel event
     */
    @FXML
    private void zoom(ScrollEvent event) {
        if(isfileLoaded) //Only usable when a file is loaded
        { 
            if(event.getDeltaY()<0)//zoom out
            {
                view.zoom(0.5,model.getLines()); //zoom out
            }
            else //zoom in
            {
                view.zoom(1.5,model.getLines()); //zoom in
            }
        }

    }

    //Panning
    /**
     * Pans the map by dragging it with the mouse
     * @param event mouseEvent 
     */
    @FXML
    public void pan(MouseEvent event) {
        if(isfileLoaded) //Only usable when a file is loaded
        {
            //Stores release location of the mouse
            releasedX = event.getX();
            releasedY = event.getY();

            //mouse distance moved
            double xResult = releasedX - pressedX;
            double yResult = releasedY - pressedY;

            //Add distance moved to translate factor.
            xTranslate += xResult;
            yTranslate += yResult;

            //Reassign scale factor and translate before drawing
            view.pan(xTranslate, yTranslate, model.getLines());
        }
    }


    /**
     *  Stores the start x,y coordinates of the mouse
     *  This is used to calculate the distance moved by the mouse 
     *  when the map is being panned
     */
    @FXML
    private void getMouseCoords(MouseEvent event) {
        pressedX = event.getX();
        pressedY = event.getY();
    }


    /**
     * Terminates the program
     */
    @FXML
    private void close() {
        System.exit(0);
    }

    /**
     * Load a file that contains information of a map
     */
    @FXML
    private void loadMap() {
        model.clearList(); 
        File lastUsed =  model.getLastUsed(); //IoHandler.getFile: used to open directory of last opened file
        File locatedFile = view.locateFile(lastUsed);

        if(locatedFile!= null) //in case if user press chancel
        {
            // Load file
            isfileLoaded = true;
            model.readFile(locatedFile); 

            //Init view
            view.setBBox(model.getBoundingBox());
            view.setLonLat(model.getMinLon(),model.getMaxLat());
            view.resetTransform();
            view.draw(model.getLines());
        }
    }

    /**
     * Display about message
     */
    @FXML
    private void about() {
        view.dialogMessages("info", "Version 0.1 - Angry vitamins \n Made by Vitamin C");
    }
}

