package ch.fhnw.huffman.commands;

import org.junit.jupiter.api.Test;

import static ch.fhnw.huffman.commands.DecodeCommand.removeFiller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ch.fhnw.huffman.commands.DecodeCommand.stringifyEncodedText;

public class DecodeCommandTest {
  @Test
  public void testStringifyEncodedText_0() {
    byte[] encodedText = {0};

    String res = stringifyEncodedText(encodedText);

    assertEquals("00000000", res);
  }

  @Test
  public void testStringifyEncodedText_n128() {
    byte[] encodedText = {(byte) 0b1000_0000};

    String res = stringifyEncodedText(encodedText);

    assertEquals("10000000", res);
  }

  @Test
  public void testStringifyEncodedText_127() {
    byte[] encodedText = {0b0111_1111};

    String res = stringifyEncodedText(encodedText);

    assertEquals("01111111", res);
  }

  @Test
  public void testRemoveFiller_lastBit1() {
    String in = "11000000";
    String res = removeFiller(in);
    assertEquals("1", res);
  }

  @Test
  public void testRemoveFiller_lastBit0() {
    String in = "01000000";
    String res = removeFiller(in);
    assertEquals("0", res);
  }

  @Test
  public void testRemoveFiller_oneBitFiller() {
    String in = "1111111";
    String res = removeFiller(in);
    assertEquals("111111", res);
  }

  @Test
  public void testRemoveFiller_fullByteFiller() {
    String in = "1111111110000000";

    String res = removeFiller(in);

    assertEquals("11111111", res);
  }
}
