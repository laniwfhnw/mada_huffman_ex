package ch.fhnw.huffman;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static ch.fhnw.huffman.HuffmanEncoding.encodeInput;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HuffmanEncodingTest {

  @Test
  public void testEncodeInput_singleChar_encodedBitString() {
    String input = "a";
    Map<Integer, String> encoding = Map.of((int) 'a', "0");

    String res = encodeInput(input, encoding);

    assertEquals("0", res);
  }

  @Test
  public void testStringifiedEncoding_twoEntries_concatedString() {
    HuffmanEncoding encMock = mock(HuffmanEncoding.class);
    when(encMock.getEncoding()).thenReturn(
        Map.of((int) 'a', "0", (int) 'b', "1"));
    when(encMock.stringifiedEncoding()).thenCallRealMethod();

    String res = encMock.stringifiedEncoding();

    assertThat(res, anyOf(is("97:0-98:1"), is("98:1-97:0")));
  }
}
