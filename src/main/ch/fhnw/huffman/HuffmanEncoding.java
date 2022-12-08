package ch.fhnw.huffman;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class HuffmanEncoding {
  private final String plainText;
  private final Map<Integer, String> encoding;
  private final String encodedText;

  public static HuffmanEncoding fromPlainText(String plainText) {
    return new HuffmanEncoding(plainText);
  }

  public static HuffmanEncoding fromEncoding(String encoding,
                                             String encodedText) {
    Map<Integer, String> encodingMap = new HashMap<>();
    Arrays.stream(encoding.split("-")).forEach(e -> {
      String[] encodingPart = e.split(":");
      encodingMap.put(Integer.parseInt(encodingPart[0]), encodingPart[1]);
    });
    return new HuffmanEncoding(encodingMap, encodedText);
  }

  public HuffmanEncoding(String plainText) {
    this.plainText = plainText;
    this.encoding =
        computeEncodingTable(
            computeEncodingTree(buildOccurrenceTable(plainText)));
    this.encodedText = encodeInput(encoding, plainText);
  }

  public HuffmanEncoding(Map<Integer, String> encoding, String encodedText) {
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

  public String stringifiedEncoding() {
    return getEncoding().entrySet().parallelStream()
                        .map(e -> String.format("%d:%s", e.getKey(),
                                                e.getValue()))
                        .collect(Collectors.joining("-"));
  }

  public String getEncodedText() {
    return encodedText;
  }

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

  @VisibleForTesting
  static String encodeInput(Map<Integer, String> encoding, String input) {
    StringBuilder encodedT = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      encodedT.append(encoding.get((int) input.charAt(i)));
    }
    return encodedT.toString();
  }

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

  @VisibleForTesting
  static class OccurrenceItem {
    @VisibleForTesting final Integer count;
    @VisibleForTesting final HuffmanNode node;

    public OccurrenceItem(Integer count, HuffmanNode node) {
      this.count = count;
      this.node = node;
    }
  }

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
