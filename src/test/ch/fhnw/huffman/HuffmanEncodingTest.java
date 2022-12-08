package ch.fhnw.huffman;

import ch.fhnw.huffman.HuffmanEncoding.OccurrenceItem;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static ch.fhnw.huffman.HuffmanEncoding.buildOccurrenceTable;
import static ch.fhnw.huffman.HuffmanEncoding.decodeText;
import static ch.fhnw.huffman.HuffmanEncoding.encodeInput;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HuffmanEncodingTest {

  @Test
  public void testStringifiedEncoding_twoEntries_concatedString() {
    HuffmanEncoding encMock = mock(HuffmanEncoding.class);
    when(encMock.getEncoding()).thenReturn(
        Map.of((int) 'a', "0", (int) 'b', "1"));
    when(encMock.stringifiedEncoding()).thenCallRealMethod();

    String res = encMock.stringifiedEncoding();

    assertThat(res, anyOf(is("97:0-98:1"), is("98:1-97:0")));
  }

  @Test
  public void testDecodeText() {
    Map<Integer, String> encoding =
        Map.of((int) 'a', "1", (int) 'b', "01", (int) 'c', "001", (int) 'd',
               "0001");
    String encodedText = "10100100010001001011";

    String res = decodeText(encoding, encodedText);

    assertEquals("abcddcba", res);
  }

  @Test
  public void testEncodeInput() {
    Map<Integer, String> encoding =
        Map.of((int) 'a', "1", (int) 'b', "01", (int) 'c', "001", (int) 'd',
               "0001");
    String input = "abcddcba";

    String res = encodeInput(encoding, input);

    assertEquals("10100100010001001011", res);
  }

  @Test
  public void testBuildOccurrenceTable() {
    String input = "The quick brown fox jumps over the lazy dog.";

    List<OccurrenceItem> res = buildOccurrenceTable(input);

    assertEquals(1, getOccurenceCountByChar(res, 'T'));
    assertEquals(2, getOccurenceCountByChar(res, 'h'));
    assertEquals(3, getOccurenceCountByChar(res, 'e'));
    assertEquals(8, getOccurenceCountByChar(res, ' '));
    assertEquals(1, getOccurenceCountByChar(res, 'q'));
    assertEquals(2, getOccurenceCountByChar(res, 'u'));
    assertEquals(1, getOccurenceCountByChar(res, 'i'));
    assertEquals(1, getOccurenceCountByChar(res, 'c'));
    assertEquals(1, getOccurenceCountByChar(res, 'k'));
    assertEquals(1, getOccurenceCountByChar(res, 'b'));
    assertEquals(2, getOccurenceCountByChar(res, 'r'));
    assertEquals(4, getOccurenceCountByChar(res, 'o'));
    assertEquals(1, getOccurenceCountByChar(res, 'w'));
    assertEquals(1, getOccurenceCountByChar(res, 'n'));
    assertEquals(1, getOccurenceCountByChar(res, 'f'));
    assertEquals(1, getOccurenceCountByChar(res, 'x'));
    assertEquals(1, getOccurenceCountByChar(res, 'j'));
    assertEquals(2, getOccurenceCountByChar(res, 'u'));
    assertEquals(1, getOccurenceCountByChar(res, 'm'));
    assertEquals(1, getOccurenceCountByChar(res, 'p'));
    assertEquals(1, getOccurenceCountByChar(res, 's'));
    assertEquals(1, getOccurenceCountByChar(res, 'v'));
    assertEquals(1, getOccurenceCountByChar(res, 't'));
    assertEquals(1, getOccurenceCountByChar(res, 'l'));
    assertEquals(1, getOccurenceCountByChar(res, 'a'));
    assertEquals(1, getOccurenceCountByChar(res, 'z'));
    assertEquals(1, getOccurenceCountByChar(res, 'y'));
    assertEquals(1, getOccurenceCountByChar(res, 'd'));
    assertEquals(1, getOccurenceCountByChar(res, 'g'));
  }

  // --- HELPER METHODS ---

  /**
   * Returns count of occurrences for a given character in the table. Returns -1
   * if character is not in table.
   *
   * @param table Table of occurrences.
   * @param c     Character with occurrence count in table.
   * @return Occurrence count of character c in table.
   */
  private static int getOccurenceCountByChar(List<OccurrenceItem> table,
                                             char c) {
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).node.c == c) {
        return table.get(i).count;
      }
    }
    return -1;
  }
}
