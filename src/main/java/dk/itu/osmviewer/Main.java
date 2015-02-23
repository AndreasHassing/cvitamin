package dk.itu.osmviewer;

/**
 * Combines the model, view and controller and runs it all neatly.
 */
public final class Main {

  /** Hidden! */
  private Main() { }

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
    System.out.println("The program runs. Woop woop!");
  }
}

