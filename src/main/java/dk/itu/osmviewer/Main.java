package dk.itu.osmviewer;

// JavaFX utils
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

// FXML utils
import javafx.fxml.FXMLLoader;

import dk.itu.osmviewer.controller.ErrorController;

/**
 * Main class.
 */
public final class Main extends Application {

  /**
   * Returns the integer it gets as input. It is indeed,
   * a silly function!
   * @param x input
   * @return just return x
   */
  public static int sillyFunction(final int x) {
    return x;
  }

  /**
   * Runs the program.
   * @param args a string array of arguments passed to the program.
   */
  public static void main(final String[] args) {
    // Launch JavaFX Application
    Main.launch();
  }

  /**
   * Start JavaFX and move into the main controller.
   * @param primaryStage The primary stage of the application.
   */
  @Override
  public void start(final Stage primaryStage) {
    Thread.currentThread().setUncaughtExceptionHandler(
      // Recklessly stolen from Kasper Isager (for displaying purposes)
      ErrorController::crash
    );

    try {
      FXMLLoader fxmlLoader = new FXMLLoader();

      Parent root = fxmlLoader.load(
        this.getClass().getResource("view/Application.fxml")
      );

      Scene scene = new Scene(root);

      primaryStage.setTitle("C-Vitamin Danmarkskort");
      primaryStage.setWidth(800);
      primaryStage.setHeight(600);
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.show();
    } catch (Throwable t) {
      // Recklessly stolen from Kasper Isager (for displaying purposes)
      ErrorController.crash(Thread.currentThread(), t);
    }
  }
}

