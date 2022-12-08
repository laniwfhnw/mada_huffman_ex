package ch.fhnw.huffman.commands;

import ch.fhnw.huffman.HuffmanEncoding;

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

    byte[] encodedText = readBytes(encodedTextPath);
    StringBuilder bitSeq = new StringBuilder();
    for (byte b : encodedText) {
      bitSeq.append(Integer.toBinaryString(b & 0xFF));
    }
    // Remove filler
    bitSeq.delete(bitSeq.lastIndexOf("1"), bitSeq.length());

    write(outputPath,
          HuffmanEncoding.fromEncoding(readString(encodingSchemePath),
                                       bitSeq.toString())
                         .getPlainText());
    return true;
  }
}
