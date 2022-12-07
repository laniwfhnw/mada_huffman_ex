package ch.fhnw.huffman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class HuffmanEncoding {
  private final Map<Integer, String> encoding;

  public static HuffmanEncoding create(String input) {
    return new HuffmanEncoding(input);
  }

  public HuffmanEncoding(String input) {
    this.encoding = computeEncoding(buildOccurrenceTable(input));
  }

  private List<OccurrenceItem> buildOccurrenceTable(String input) {
    Map<Integer, Integer> occurrences = new HashMap<>();
    input.chars().forEach(c -> {
      if (occurrences.containsKey(c)) {
        occurrences.replace(c, occurrences.get(c) + 1);
      } else {occurrences.put(c, 1);}
    });
    return occurrences.entrySet().stream()
                      .map(e -> new OccurrenceItem(e.getValue(),
                                                   HuffmanNode.createLeaf(
                                                       e.getKey()))).toList();
  }

  private Map<Integer, String> computeEncoding(
      List<OccurrenceItem> o) {
    HuffmanNode encodingTree = computeEncodingTree(o);
    return computeEncodingTable(encodingTree);
  }

  private HuffmanNode computeEncodingTree(List<OccurrenceItem> o) {
    while (o.size() > 1) {
      int lowestI = 0;
      for (int i = 1; i < o.size(); i++) { // skip [0]
        if (o.get(i).count < o.get(lowestI).count) {
          lowestI = i;
        }
      }
      int secondLowestI = lowestI == 0 ? 1 : 0;
      for (int i = 1; i < o.size(); i++) { // skip [0]
        if (lowestI != i && o.get(i).count < o.get(lowestI).count) {
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

  private Map<Integer, String> computeEncodingTable(HuffmanNode node) {
    if (node.isLeaf()) {
      throw new IllegalArgumentException("Can't work with single-node tree.");
    }
    Map<Integer, String> encoding = new HashMap<>();
    StringBuilder currE = new StringBuilder();
    Stack<HuffmanNode> parents = new Stack<>();
    HuffmanNode currN = node.left;

    if (currN.isLeaf()) {
      encoding.put(currN.c, currE.toString());
    } else {
      if (currN == parents.peek().left) {
        currN = parents.peek().right;
      }
    }
  }

  private static class OccurrenceItem {
    private final Integer count;
    private final HuffmanNode node;

    public OccurrenceItem(Integer count, HuffmanNode node) {
      this.count = count;
      this.node = node;
    }
  }

  private static class HuffmanNode {
    private final HuffmanNode left;
    private final HuffmanNode right;
    private final Integer c;

    private static HuffmanNode createBranch(HuffmanNode left,
                                            HuffmanNode right) {
      return new HuffmanNode(left, right, null);
    }

    private static HuffmanNode createLeaf(Integer value) {
      return new HuffmanNode(null, null, value);
    }

    private boolean isBranch() {
      return this.c == null;
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
