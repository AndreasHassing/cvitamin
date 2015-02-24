package dk.itu.osmviewer.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Error controller class.
 *
 * @version 1.0.0
 */
public final class ErrorController {
  /**
   * The singleton instance of the controller.
   */
  private static ErrorController instance;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Error controller.
   */
  public ErrorController getInstance() {
    return ErrorController.instance;
  }

  /**
   * Initialize the controller.
   *
   * @throws Exception In case of uncaught errors.
   */
  public void initialize() throws Exception {
    ErrorController.instance = this;
  }

  /**
   * Display a "Try again" error message.
   */
  public static void tryAgain() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Some Message");
    alert.setHeaderText("Testing");
    alert.setContentText("wtf?");
    alert.showAndWait();
  }

  /**
   * "Graceful" crash handling.
   *
   * @param thread    The thread in which the crash happened.
   * @param throwable The exception that caused the crash.
   */
  public static void crash(final Thread thread, final Throwable throwable) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Some Message");
    alert.setHeaderText("Testing");
    alert.setContentText(throwable.getMessage());
    alert.showAndWait();

    System.exit(1);
  }
}
