package ch.fhnw.huffman.commands;

import ch.fhnw.huffman.HuffmanEncoding;
import com.google.common.annotations.VisibleForTesting;

import java.nio.file.Path;
import java.util.Scanner;

import static ch.fhnw.huffman.FileUtil.readBytes;
import static ch.fhnw.huffman.FileUtil.readString;
import static ch.fhnw.huffman.FileUtil.write;
import static ch.fhnw.huffman.commands.CommandUtil.askForExistingPath;
import static ch.fhnw.huffman.commands.CommandUtil.askForOutputPath;

public class DecodeCommand implements Command {
  @Override public boolean execute(Scanner s) {
    Path encodingSchemePath = askForExistingPath(s, "encoding scheme");
    Path encodedTextPath = askForExistingPath(s, "encoded text");
    Path outputPath = askForOutputPath(s, "decoded text");

    String stringifiedEncodedText =
        stringifyEncodedText(readBytes(encodedTextPath));
    String cleanedEncodedText = removeFiller(stringifiedEncodedText);

    write(outputPath,
          HuffmanEncoding.fromEncoding(readString(encodingSchemePath),
                                       cleanedEncodedText).getPlainText());
    return true;
  }

  @VisibleForTesting
  static String stringifyEncodedText(byte[] encodedText) {
    StringBuilder bitSeq = new StringBuilder();
    for (byte b : encodedText) {
      bitSeq.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)));
    }
    return bitSeq.toString().replace(' ', '0');
  }

  @VisibleForTesting
  static String removeFiller(String encodedText) {
    return encodedText.substring(0, encodedText.lastIndexOf('1'));
  }
}
