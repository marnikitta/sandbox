package org.stepik.marshalkin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class AndExpression<T extends Expression> implements Expression {
  private final Set<T> operands;

  public AndExpression(final T first, final T second) {
    this(Arrays.asList(first, second));
  }

  public AndExpression(final Collection<? extends T> operands) {
    if (operands.isEmpty()) {
      throw new IllegalArgumentException("There should be at least one operand");
    }
    this.operands = new HashSet<>(operands);
  }

  public AndExpression(final T first) {
    this(Collections.singleton(first));
  }

  public Set<T> operands() {
    return Collections.unmodifiableSet(this.operands);
  }

  @Override
  public OrExpression<AndExpression<Primary>> normalized() {
    final List<Set<AndExpression<Primary>>> collectinified = this.operands.stream()
            .map(T::normalized)
            .map(OrExpression::operands)
            .collect(Collectors.toList());

    final Set<List<AndExpression<Primary>>> multiplied = NotExpression.cartesian(collectinified);

    final Set<AndExpression<Primary>> flattened = multiplied.stream()
            .map(li -> li.stream().map(AndExpression::operands).flatMap(Set::stream).collect(Collectors.toList()))
            .map(AndExpression::new)
            .collect(Collectors.toSet());

    return new OrExpression<>(flattened);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.operands);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final AndExpression<?> that = (AndExpression<?>) o;
    return Objects.equals(this.operands, that.operands);
  }

  @Override
  public String toString() {
    return this.operands.stream().map(T::toString).collect(Collectors.joining(" AND "));
  }
}
