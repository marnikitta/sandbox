package ru.jetbrains.marnikitta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImmutableClass {
  public static class MetaProp {
    public final int prop1;
    public final int prop2;

    public MetaProp(int prop1, int prop2) {
      this.prop1 = prop1;
      this.prop2 = prop2;
    }
  }

  private final int a;
  private String b;
  private final List<MetaProp> metas;

  public ImmutableClass(int a, String b, List<MetaProp> metas) {
    this.a = a;
    this.b = b;
    this.metas = new ArrayList<>(metas);

  }

  public int getA() {
    return a;
  }

  public String getB() {
    return b;
  }

  public List<MetaProp> getMetas() {
    return Collections.unmodifiableList(metas);
  }
}