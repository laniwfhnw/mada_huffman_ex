package ch.fhnw.huffman;

import ch.fhnw.huffman.HuffmanEncoding.HuffmanNode;
import ch.fhnw.huffman.HuffmanEncoding.OccurrenceItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.fhnw.huffman.HuffmanEncoding.HuffmanNode.createBranch;
import static ch.fhnw.huffman.HuffmanEncoding.HuffmanNode.createLeaf;
import static ch.fhnw.huffman.HuffmanEncoding.buildOccurrenceTable;
import static ch.fhnw.huffman.HuffmanEncoding.computeEncodingTable;
import static ch.fhnw.huffman.HuffmanEncoding.computeEncodingTree;
import static ch.fhnw.huffman.HuffmanEncoding.decodeText;
import static ch.fhnw.huffman.HuffmanEncoding.encodeInput;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  }

  @Test
  public void testComputeEncodingTree_noOccurences_throwsIAE() {
    IllegalArgumentException iae =
        assertThrows(IllegalArgumentException.class,
                     () -> computeEncodingTree(List.of()));
    assertEquals("Can't calculate encoding tree with no occurrences.",
                 iae.getMessage());
  }

  @Test
  public void testComputeEncodingTree_threeEquallyImportantOccurences_orderDictatesTreeStructure() {
    List<OccurrenceItem> occurrences = new ArrayList<>(
        List.of(new OccurrenceItem(1, createLeaf((int) 'a')),
                new OccurrenceItem(1, createLeaf((int) 'b')),
                new OccurrenceItem(2, createLeaf((int) 'c')),
                new OccurrenceItem(3, createLeaf((int) 'd'))));

    HuffmanNode res = computeEncodingTree(occurrences);

    assertEquals('a', res.right.left.left.c);
    assertEquals('b', res.right.left.right.c);
    assertEquals('c', res.right.right.c);
    assertEquals('d', res.left.c);
  }

  @Test
  public void testComputeEncodingTree_occurrencesSortedHighToLow() {
    List<OccurrenceItem> occurrences = new ArrayList<>(
        List.of(
            new OccurrenceItem(6, createLeaf((int) 'd')),
            new OccurrenceItem(3, createLeaf((int) 'c')),
            new OccurrenceItem(1, createLeaf((int) 'a')),
            new OccurrenceItem(1, createLeaf((int) 'b'))));

    HuffmanNode res = computeEncodingTree(occurrences);

    assertEquals('a', res.left.left.left.c);
    assertEquals('b', res.left.left.right.c);
    assertEquals('c', res.left.right.c);
    assertEquals('d', res.right.c);
  }

  @Test
  public void testComputeEncodingTree_occurrencesUnsorted() {
    List<OccurrenceItem> occurrences = new ArrayList<>(
        List.of(
            new OccurrenceItem(2, createLeaf((int) 'b')),
            new OccurrenceItem(1, createLeaf((int) 'a')),
            new OccurrenceItem(4, createLeaf((int) 'c'))));

    HuffmanNode res = computeEncodingTree(occurrences);

    assertEquals('a', res.left.left.c);
    assertEquals('b', res.left.right.c);
    assertEquals('c', res.right.c);
  }

  @Test
  public void testComputeEncodingTable_oneSidedTree() {
    HuffmanNode a = createLeaf((int) 'a');
    HuffmanNode b = createLeaf((int) 'b');
    HuffmanNode c = createLeaf((int) 'c');
    HuffmanNode root = createBranch(createBranch(a, b), c);

    Map<Integer, String> res = computeEncodingTable(root);

    assertEquals(3, res.size());
    assertEquals("00", res.get((int) 'a'));
    assertEquals("01", res.get((int) 'b'));
    assertEquals("1", res.get((int) 'c'));
  }

  @Test
  public void testComputeEncodingTable_balancedTree() {
    HuffmanNode a = createLeaf((int) 'a');
    HuffmanNode b = createLeaf((int) 'b');
    HuffmanNode c = createLeaf((int) 'c');
    HuffmanNode d = createLeaf((int) 'd');
    HuffmanNode root = createBranch(createBranch(a, b), createBranch(c, d));

    Map<Integer, String> res = computeEncodingTable(root);

    assertEquals(4, res.size());
    assertEquals("00", res.get((int) 'a'));
    assertEquals("01", res.get((int) 'b'));
    assertEquals("10", res.get((int) 'c'));
    assertEquals("11", res.get((int) 'd'));
  }

  // --- HELPER METHODS ---

  /**
   * Returns count of occurrences for a given character in the table. Returns 0
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
    return 0;
  }
}
