package ch.fhnw.huffman;

import com.google.common.annotations.VisibleForTesting;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Class to handle any Huffman-encoding-related tasks.
 */
@Immutable
public class HuffmanEncoding {
  private final String plainText;
  private final Map<Integer, String> encoding;
  private final String encodedText;

  /**
   * Public interface function to create encoding table and encode plain text
   * accordingly.
   *
   * @param plainText The text to encode.
   * @return The created instance of the huffman-encoding.
   */
  public static HuffmanEncoding fromPlainText(String plainText) {
    return new HuffmanEncoding(plainText);
  }

  /**
   * Public interface function to decode encoded text.
   *
   * @param encoding    The encoding scheme that was used to encode the plain
   *                    text into the encoded binary format.
   * @param encodedText The binary digits of the encoded text.
   * @return The created instance of the huffman-encoding.
   */
  public static HuffmanEncoding fromEncoding(String encoding,
                                             String encodedText) {
    Map<Integer, String> encodingMap = new HashMap<>();
    Arrays.stream(encoding.split("-")).forEach(e -> {
      String[] encodingPart = e.split(":");
      encodingMap.put(Integer.parseInt(encodingPart[0]), encodingPart[1]);
    });
    return new HuffmanEncoding(encodingMap, encodedText);
  }

  /**
   * Constructor for encoding text.
   *
   * @param plainText Text to be encoded.
   */
  private HuffmanEncoding(String plainText) {
    this.plainText = plainText;
    this.encoding =
        computeEncodingTable(
            computeEncodingTree(buildOccurrenceTable(plainText)));
    this.encodedText = encodeInput(encoding, plainText);
  }

  /**
   * Constructor for decoding text.
   *
   * @param encoding    Encoding scheme used to decode encoded text.
   * @param encodedText Encoded text.
   */
  private HuffmanEncoding(Map<Integer, String> encoding, String encodedText) {
    this.encoding = encoding;
    this.encodedText = encodedText;
    this.plainText = decodeText(encoding, encodedText);
  }

  public String getPlainText() {
    return plainText;
  }

  @VisibleForTesting
  Map<Integer, String> getEncoding() {
    return encoding;
  }

  /**
   * Provides encoding in a string format ready to be saved to file.
   *
   * @return String format of encoding.
   */
  public String getStringifiedEncoding() {
    return getEncoding().entrySet().parallelStream()
                        .map(e -> String.format("%d:%s", e.getKey(),
                                                e.getValue()))
                        .collect(Collectors.joining("-"));
  }

  public String getEncodedText() {
    return encodedText;
  }

  /**
   * Decodes encoded text according to original encoding scheme.
   *
   * @param encoding    Encoding scheme used for encoding.
   * @param encodedText Encoded text.
   * @return The decoded text.
   */
  @VisibleForTesting
  static String decodeText(Map<Integer, String> encoding,
                           String encodedText) {
    Map<String, Integer> reverseEncoding =
        encoding.entrySet().parallelStream()
                .collect(toMap(Entry::getValue, Entry::getKey));
    StringBuilder bitSeq = new StringBuilder(encodedText);
    StringBuilder decodedText = new StringBuilder();
    while (!bitSeq.isEmpty()) {
      boolean seqFound = false;
      for (int i = 1; i <= bitSeq.length() && !seqFound; i++) {
        String potentialKey = bitSeq.substring(0, i);
        if (reverseEncoding.containsKey(potentialKey)) {
          decodedText.append(
              (char) reverseEncoding.get(potentialKey).intValue());
          bitSeq.delete(0, i);
          seqFound = true;
        }
      }
    }
    return decodedText.toString();
  }

  /**
   * Encodes plain text according to given encoding scheme.
   *
   * @param encoding Encoding scheme used for encoding.
   * @param input    Plain text.
   * @return The encoded text.
   */
  @VisibleForTesting
  static String encodeInput(Map<Integer, String> encoding, String input) {
    StringBuilder encodedT = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      encodedT.append(encoding.get((int) input.charAt(i)));
    }
    return encodedT.toString();
  }

  /**
   * Builds table of occurrences for a given plain text string.
   *
   * @param input Plain text string.
   * @return Occurrence table with HuffmanNodes as elements.
   */
  @VisibleForTesting
  static List<OccurrenceItem> buildOccurrenceTable(String input) {
    Map<Integer, Integer> occurrences = new HashMap<>();
    input.chars().forEach(c -> {
      if (occurrences.containsKey(c)) {
        occurrences.replace(c, occurrences.get(c) + 1);
      } else {occurrences.put(c, 1);}
    });
    return occurrences.entrySet().parallelStream()
                      .map(e -> new OccurrenceItem(e.getValue(),
                                                   HuffmanNode.createLeaf(
                                                       e.getKey()))).collect(
            Collectors.toCollection(ArrayList::new));
  }

  /**
   * Builds the encoding tree from the occurrence table. If there are multiple
   * elements with the same occurrence probability the order dictates which of
   * them are "less-likely"; the ones with a lower index are then considered
   * less likely to occur.
   *
   * @param o Occurrence table.
   * @return The root node of the encoding tree.
   */
  @VisibleForTesting
  static HuffmanNode computeEncodingTree(List<OccurrenceItem> o) {
    if (o.size() == 0) {
      throw new IllegalArgumentException(
          "Can't calculate encoding tree with no occurrences.");
    }
    while (o.size() > 1) {
      int lowestI = 0;
      for (int i = 1; i < o.size(); i++) { // skip [0]
        if (o.get(i).count < o.get(lowestI).count) {
          lowestI = i;
        }
      }
      int secondLowestI = lowestI == 0 ? 1 : 0;
      for (int i = 1; i < o.size(); i++) { // skip [0]
        if (lowestI != i && o.get(i).count < o.get(secondLowestI).count) {
          secondLowestI = i;
        }
      }
      // Combine lowest occurrence items
      o.set(lowestI, new OccurrenceItem(
          o.get(lowestI).count + o.get(secondLowestI).count,
          HuffmanNode.createBranch(o.get(lowestI).node,
                                   o.get(secondLowestI).node)));
      o.remove(secondLowestI);
    }
    return o.get(0).node;
  }

  /**
   * Computes the mapping of character to binary string for a given Huffman
   * tree.
   *
   * @param node The root node of the encoding tree.
   * @return Map from ASCII value of character to binary string encoding.
   */
  @VisibleForTesting
  static Map<Integer, String> computeEncodingTable(HuffmanNode node) {
    Map<Integer, String> encoding = new HashMap<>();
    recComputeEncodingTable(node, encoding, "");
    return encoding;
  }

  private static void recComputeEncodingTable(HuffmanNode node,
                                              Map<Integer, String> e,
                                              String currE) {
    if (node.isLeaf()) {
      e.put(node.c, currE);
    } else {
      recComputeEncodingTable(node.left, e, currE + "0");
      recComputeEncodingTable(node.right, e, currE + "1");
    }
  }

  /**
   * Represents a row in the occurrence table that is constructed to encode a
   * text.
   */
  @VisibleForTesting
  static class OccurrenceItem {
    @VisibleForTesting final Integer count;
    @VisibleForTesting final HuffmanNode node;

    public OccurrenceItem(Integer count, HuffmanNode node) {
      this.count = count;
      this.node = node;
    }
  }

  /**
   * Represents a node in the Huffman encoding tree.
   */
  @VisibleForTesting
  static class HuffmanNode {
    @VisibleForTesting final HuffmanNode left;
    @VisibleForTesting final HuffmanNode right;
    @VisibleForTesting final Integer c;

    @VisibleForTesting
    static HuffmanNode createBranch(HuffmanNode left, HuffmanNode right) {
      return new HuffmanNode(left, right, null);
    }

    @VisibleForTesting
    static HuffmanNode createLeaf(Integer value) {
      return new HuffmanNode(null, null, value);
    }

    private boolean isLeaf() {
      return this.c != null;
    }

    public HuffmanNode(HuffmanNode left, HuffmanNode right, Integer c) {
      this.left = left;
      this.right = right;
      this.c = c;
    }
  }
}
