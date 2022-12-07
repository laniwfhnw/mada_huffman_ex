package ch.fhnw.huffman.commands;

import ch.fhnw.huffman.FileUtil;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

import static ch.fhnw.huffman.ConsoleUtil.getUserInput;

class CommandUtil {
  static Path askForExistingPath(Scanner s, String contentDesc) {
    Path filePath = Path.of("");
    boolean userInputValid = false;
    while (!userInputValid) {
      System.out.printf(
          "Where is the %s stored?%n" +
              "Current working directory is \"%s\".%n> ",
          contentDesc, FileUtil.getCwd());
      try {
        filePath = Path.of(getUserInput(s));
        userInputValid = true;
      } catch (InvalidPathException ignored) {
        System.out.printf("Invalid path %s%n", filePath);
      }
      if (userInputValid &&
              !new File(filePath.toString()).exists()) {
        userInputValid = false;
        System.out.printf("File \"%s\" not found.%n", filePath);
      }
    }
    return filePath;
  }

  static Path askForOutputPath(Scanner s, String contentDesc) {
    Path filePath = Path.of("");
    boolean userInputValid = false;
    while (!userInputValid) {
      System.out.printf(
          "Where should the %s be stored?%n" +
              "Current working directory is \"%s\".%n> ",
          contentDesc, FileUtil.getCwd());
      try {
        filePath = Path.of(getUserInput(s));
        userInputValid = true;
      } catch (InvalidPathException ignored) {
        System.out.printf("Invalid path %s%n", filePath);
      }
    }
    return filePath;
  }
}
