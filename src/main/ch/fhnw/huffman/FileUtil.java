package ch.fhnw.huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Provides util functions for file operations.
 */
public class FileUtil {

  /**
   * Returns cwd so the user knows where they're working from to avoid incorrect
   * relative paths.
   *
   * @return Current working directory.
   */
  public static Path getCwd() {
    return Path.of(System.getProperty("user.dir"));
  }

  /**
   * Writes string content to file at filePath. Creates the file at filePath if
   * it doesn't exist yet. Existing files will be overwritten.
   *
   * @param filePath File path to write ti.
   * @param content  Content to write to the file.
   */
  public static void write(Path filePath, String content) {
    try {
      if (!Files.exists(filePath)) {
        Files.createFile(filePath);
      }
      Files.writeString(filePath, content, StandardOpenOption.WRITE);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException();
    }
  }

  /**
   * Writes byte array content to file at filePath. Creates the file at filePath
   * if it doesn't exist yet. Existing files will be overwritten.
   *
   * @param filePath File path to write ti.
   * @param content  Content to write to the file.
   */
  public static void write(Path filePath, byte[] content) {
    try {
      if (!Files.exists(filePath)) {
        Files.createFile(filePath);
      }
      Files.write(filePath, content, StandardOpenOption.WRITE);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException();
    }
  }

  /**
   * Returns content of file in UTF-8 string. This function assumes the file
   * exists when being called.
   *
   * @param filePath Path to the file to read.
   * @return String of characters in the file.
   */
  public static String readString(Path filePath) {
    try {
      return Files.readString(filePath);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Returns byte array content of file. This function assumes the file exists
   * when being called.
   *
   * @param filePath Path to the file to read.
   * @return byte[] file content.
   */
  public static byte[] readBytes(Path filePath) {
    try {
      return Files.readAllBytes(filePath);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
