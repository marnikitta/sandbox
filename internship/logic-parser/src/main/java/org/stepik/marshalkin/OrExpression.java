package org.stepik.marshalkin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class OrExpression<T extends Expression> implements Expression {
  private final Set<T> operands;

  public OrExpression(final T first, final T second) {
    this(Arrays.asList(first, second));
  }

  public OrExpression(final Collection<? extends T> operands) {
    if (operands.isEmpty()) {
      throw new IllegalArgumentException("There should be at least one operand");
    }
    this.operands = new HashSet<>(operands);
  }

  public OrExpression(final T first) {
    this(Collections.singleton(first));
  }

  public Set<T> operands() {
    return Collections.unmodifiableSet(this.operands);
  }

  @Override
  public OrExpression<AndExpression<Primary>> normalized() {
    return new OrExpression<>(this.operands.stream().map(Expression::normalized)
            .map(OrExpression::operands)
            .flatMap(Set::stream)
            .collect(Collectors.toSet()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.operands);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final OrExpression<?> that = (OrExpression<?>) o;
    return Objects.equals(this.operands, that.operands);
  }

  @Override
  public String toString() {
    return this.operands.stream().map(T::toString).collect(Collectors.joining(" OR "));
  }
}

