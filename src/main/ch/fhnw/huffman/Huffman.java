package ch.fhnw.huffman;

import ch.fhnw.huffman.commands.Command;
import ch.fhnw.huffman.commands.DecodeCommand;
import ch.fhnw.huffman.commands.EncodeCommand;
import ch.fhnw.huffman.commands.QuitCommand;

import java.util.Map;
import java.util.Scanner;

import static ch.fhnw.huffman.ConsoleUtil.getUserInput;

public class Huffman {
  private static final Map<Character, Command> commandMap =
      Map.of('e', new EncodeCommand(), 'd', new DecodeCommand(), 'q',
             new QuitCommand());

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    boolean userWantsToDoSth = true;
    do {
      System.out.print(
          "Would you like to [e]code, [d]ecode or [q]uit?\n> ");
      char userInput = getUserInput(s).charAt(0);
      if (commandMap.containsKey(userInput)) {
        userWantsToDoSth = commandMap.get(userInput).execute(s);
      } else {
        System.out.printf("Unexpected input %c.%n", userInput);
      }
    } while (userWantsToDoSth);
    s.close();
  }
}
