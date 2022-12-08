package ch.fhnw.huffman;

import ch.fhnw.huffman.commands.Command;
import ch.fhnw.huffman.commands.DecodeCommand;
import ch.fhnw.huffman.commands.EncodeCommand;
import ch.fhnw.huffman.commands.QuitCommand;

import java.util.Map;
import java.util.Scanner;

import static ch.fhnw.huffman.ConsoleUtil.getUserInput;

/**
 * Starting class of application.
 */
public class Huffman {
  private static final Map<Character, Command> commandMap =
      Map.of('e', new EncodeCommand(), 'd', new DecodeCommand(), 'q',
             new QuitCommand());

  /**
   * Starting function of application.
   */
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    boolean userWantsToDoSth = true;
    do {
      System.out.print(
          "Would you like to [e]ncode, [d]ecode or [q]uit?\n> ");
      String userInput = getUserInput(s);
      if (userInput.length() != 1) {
        System.out.printf("Unexpected input length %d.%n", userInput.length());
      } else if (commandMap.containsKey(userInput.charAt(0))) {
        userWantsToDoSth = commandMap.get(userInput.charAt(0)).execute(s);
      } else {
        System.out.printf("Unexpected input %c.%n", userInput.charAt(0));
      }
    } while (userWantsToDoSth);
    s.close();
  }
}
