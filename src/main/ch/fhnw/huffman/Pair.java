package ch.fhnw.huffman;

import java.util.Map.Entry;

public class Pair<T, T1> {
  private final T first;
  private final T1 second;

  public Pair(T first, T1 second) {
    this.first = first;
    this.second = second;
  }

  public Pair(Entry<T, T1> e) {
    this.first = e.getKey();
    this.second = e.getValue();
  }

  public T getFirst() {
    return first;
  }

  public T1 getSecond() {
    return second;
  }
}
