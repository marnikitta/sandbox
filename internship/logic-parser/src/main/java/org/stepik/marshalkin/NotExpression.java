package org.stepik.marshalkin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class NotExpression<T extends Expression> implements Expression {
  private final T expression;

  public NotExpression(final T expression) {
    this.expression = expression;
  }

  public static <T> Set<List<T>> cartesian(final List<? extends Set<T>> components) {
    final Set<List<T>> result = new HashSet<>();
    if (components.isEmpty()) {
      result.add(new ArrayList<>());
    } else {
      for (final T head : components.get(0)) {
        NotExpression.cartesian(components.subList(1, components.size())).stream()
                .map(tup -> {
                  tup.add(head);
                  return tup;
                })
                .forEach(result::add);
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return "NOT " + this.expression;
  }

  @Override
  public OrExpression<AndExpression<Primary>> normalized() {
    final OrExpression<AndExpression<Primary>> dnf = this.expression.normalized();

    final List<Set<Primary>> collectinified = dnf.operands().stream()
            .map(and -> and.operands().stream().map(Primary::negated).collect(Collectors.toSet()))
            .collect(Collectors.toList());

    final Set<List<Primary>> multiplied = NotExpression.cartesian(collectinified);

    final Set<AndExpression<Primary>> disjunctions = multiplied.stream()
            .map(AndExpression::new)
            .collect(Collectors.toSet());

    return new OrExpression<>(disjunctions);
  }
}
