package ch.fhnw.huffman;

import ch.fhnw.huffman.commands.Command;
import ch.fhnw.huffman.commands.DecodeCommand;
import ch.fhnw.huffman.commands.EncodeCommand;
import ch.fhnw.huffman.commands.QuitCommand;

import java.util.Map;

public class Huffman {
  private static final Map<Character, Command> commandMap =
      Map.of('e', new EncodeCommand(), 'd', new DecodeCommand(), 'q',
             new QuitCommand());

  public static void main(String[] args) {
    System.out.println("Hello World!");
  }
}
