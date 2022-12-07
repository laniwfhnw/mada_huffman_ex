package ch.fhnw.huffman.commands;

import java.util.Scanner;

public class QuitCommand implements Command {
  @Override public boolean execute(Scanner s) {
    return false;
  }
}
