package ch.fhnw.huffman;

import java.util.Scanner;

public class ConsoleUtil {
  /**
   * Gets one line from the command line and returns it.
   *
   * @param s Scanner used for the whole application.
   * @return The Line the user entered.
   */
  public static String getUserInput(Scanner s) {
    return s.nextLine();
  }
}
