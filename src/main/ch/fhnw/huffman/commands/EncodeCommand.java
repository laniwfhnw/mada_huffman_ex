package ch.fhnw.huffman.commands;

import ch.fhnw.huffman.HuffmanEncoding;

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

    HuffmanEncoding encoding = HuffmanEncoding.fromPlainText(readString(plainTextPath));
    write(encodingSchemeOutPath, encoding.stringifiedEncoding());
    write(encodedTextOutPath, padEncodedText(encoding.getEncodedText()));
    return true;
  }

  private byte[] padEncodedText(String encodedText) {
    StringBuilder paddedEncodedText = new StringBuilder(encodedText);
    paddedEncodedText.append("1");
    paddedEncodedText.append("0".repeat(8 - (paddedEncodedText.length() % 8)));
    byte[] encodedBytes = new byte[paddedEncodedText.length() / 8];
    for (int i = 0; i < encodedBytes.length; i++) {
      encodedBytes[i] = (byte) Integer.parseInt(paddedEncodedText.substring(0, 8), 2);
      paddedEncodedText.delete(0, 8);
    }
    return encodedBytes;
  }
}
