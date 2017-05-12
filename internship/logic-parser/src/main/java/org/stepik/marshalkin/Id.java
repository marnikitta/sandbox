package org.stepik.marshalkin;

import java.util.Objects;

public final class Id implements Primary {
  private final CharSequence name;

  public Id(final CharSequence name) {
    this.name = name;
  }

  @Override
  public OrExpression<AndExpression<Primary>> normalized() {
    return new OrExpression<>(new AndExpression<>(this));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final Id id = (Id) o;
    return Objects.equals(this.name, id.name);
  }

  @Override
  public String toString() {
    return this.name.toString();
  }

  @Override
  public Primary negated() {
    return new NegatedId(this.name);
  }
}
