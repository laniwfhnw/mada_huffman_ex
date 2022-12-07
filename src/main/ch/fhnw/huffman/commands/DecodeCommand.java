package ch.fhnw.huffman.commands;

import ch.fhnw.huffman.FileUtil;
import ch.fhnw.huffman.HuffmanEncoding;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

import static ch.fhnw.huffman.ConsoleUtil.getUserInput;
import static ch.fhnw.huffman.FileUtil.readBytes;
import static ch.fhnw.huffman.FileUtil.readString;
import static ch.fhnw.huffman.FileUtil.write;

public class DecodeCommand implements Command {
  @Override public boolean execute(Scanner s) {
    Path encodingSchemePath = askForExistingPath(s, "encoding scheme");
    Path encodedTextPath = askForExistingPath(s, "encoded text");
    Path outputPath = askForOutputPath(s, "decoded text");

    byte[] encodedText = readBytes(encodedTextPath);
    StringBuilder bitSeq = new StringBuilder();
    for (byte b : encodedText) {
      bitSeq.append(Integer.toBinaryString(b));
    }
    // Remove filler
    bitSeq.delete(bitSeq.lastIndexOf("1"), bitSeq.length());

    write(outputPath,
          HuffmanEncoding.fromEncoding(readString(encodingSchemePath), bitSeq.toString())
                         .getPlainText());
    return true;
  }

  private static Path askForExistingPath(Scanner s, String contentDesc) {
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

  private static Path askForOutputPath(Scanner s, String contentDesc) {
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
