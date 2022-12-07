package ch.fhnw.huffman;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static ch.fhnw.huffman.HuffmanEncoding.encodeInput;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanEncodingTest {

  @Test
  public void testEncodeInput_singleChar_mapsCorrectly() {
    String input = "a";
    Map<Integer, String> encoding = Map.of((int) 'a', "0");

    String encodedInput = encodeInput(input, encoding);

    assertEquals("0", encodedInput);
  }

}
