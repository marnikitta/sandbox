package org.stepik.marshalkin;

import java.util.Objects;

public final class NegatedId implements Primary {
  private final CharSequence name;

  public NegatedId(final CharSequence name) {
    this.name = name;
  }

  @Override
  public OrExpression<AndExpression<Primary>> normalized() {
    return new OrExpression<>(new AndExpression<>(this));
  }

  @Override
  public Primary negated() {
    return new Id(this.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final NegatedId negatedId = (NegatedId) o;
    return Objects.equals(this.name, negatedId.name);
  }

  @Override
  public String toString() {
    return "NOT " + this.name;
  }
}

