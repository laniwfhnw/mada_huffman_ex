package ch.fhnw.huffman.commands;

import ch.fhnw.huffman.HuffmanEncoding;
import com.google.common.annotations.VisibleForTesting;

import java.nio.file.Path;
import java.util.Scanner;

import static ch.fhnw.huffman.FileUtil.readString;
import static ch.fhnw.huffman.FileUtil.write;
import static ch.fhnw.huffman.commands.CommandUtil.askForExistingPath;
import static ch.fhnw.huffman.commands.CommandUtil.askForOutputPath;

public class EncodeCommand implements Command {
  @Override public boolean execute(Scanner s) {
    Path plainTextPath = askForExistingPath(s, "text");
    Path encodingSchemeOutPath = askForOutputPath(s, "encoding scheme");
    Path encodedTextOutPath = askForOutputPath(s, "encoded text");

    HuffmanEncoding encoding =
        HuffmanEncoding.fromPlainText(readString(plainTextPath));
    write(encodingSchemeOutPath, encoding.getStringifiedEncoding());
    write(encodedTextOutPath,
          binStringToByteArr(padEncodedText(encoding.getEncodedText())));
    return true;
  }

  @VisibleForTesting
  static String padEncodedText(String encodedText) {
    return encodedText + "1" +
               "0".repeat((8 - ((encodedText.length() + 1) % 8)) % 8);
  }

  @VisibleForTesting
  static byte[] binStringToByteArr(String paddedEncodedText) {
    byte[] encodedBytes = new byte[paddedEncodedText.length() / 8];
    for (int i = 0; i < encodedBytes.length; i++) {
      encodedBytes[i] =
          (byte) Integer.parseInt(paddedEncodedText.substring(0, 8), 2);
      paddedEncodedText = paddedEncodedText.substring(8);
    }
    return encodedBytes;
  }
}
