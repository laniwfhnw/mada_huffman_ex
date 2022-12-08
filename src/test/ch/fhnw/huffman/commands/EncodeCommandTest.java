package ch.fhnw.huffman.commands;

import org.junit.jupiter.api.Test;

import static ch.fhnw.huffman.commands.EncodeCommand.binStringToByteArr;
import static ch.fhnw.huffman.commands.EncodeCommand.padEncodedText;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodeCommandTest {
  @Test
  public void testPadEncodedText_padsPerfectSequence() {
    String in = "11110000";
    String res = padEncodedText(in);
    assertEquals("1111000010000000", res);
  }

  @Test
  public void testPadEncodedText_padsOneBit() {
    String in = "1111000";
    String res = padEncodedText(in);
    assertEquals("11110001", res);
  }

  @Test
  public void testPadEncodedText_pads4Bits() {
    String in = "1111";
    String res = padEncodedText(in);
    assertEquals("11111000", res);
  }

  @Test
  public void testBinStringToByteArr_zeroByte() {
    String in = "00000000";
    byte[] res = binStringToByteArr(in);
    assertArrayEquals(new byte[] {0}, res);
  }

  @Test
  public void testBinStringToByteArr_negativeByte() {
    String in = "10000000";
    byte[] res = binStringToByteArr(in);
    assertArrayEquals(new byte[] {-128}, res);
  }

  @Test
  public void testBinStringToByteArr_positveByte() {
    String in = "01000000";
    byte[] res = binStringToByteArr(in);
    assertArrayEquals(new byte[] {64}, res);
  }

  @Test
  public void testBinStringToByteArr_twoZebraBytes() {
    String in = "1010101001010101";
    byte[] res = binStringToByteArr(in);
    assertArrayEquals(new byte[] {(byte) -86, 85}, res);
  }

  @Test
  public void testBinStringToByteArr_twoZeroBytes() {
    String in = "0000000000000000";
    byte[] res = binStringToByteArr(in);
    assertArrayEquals(new byte[] {0, 0}, res);
  }
}
