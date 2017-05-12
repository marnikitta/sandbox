package org.stepik.marshalkin.simplifier;

import org.stepik.marshalkin.AndExpression;
import org.stepik.marshalkin.Literal;
import org.stepik.marshalkin.Primary;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AndSimplifier implements Function<AndExpression<Primary>, AndExpression<Primary>> {
  @Override
  public AndExpression<Primary> apply(final AndExpression<Primary> conjunct) {
    return AndSimplifier.collapsedNegates(AndSimplifier.filteredNeutral(AndSimplifier.falsified(conjunct)));
  }

  private static AndExpression<Primary> collapsedNegates(final AndExpression<Primary> conjunct) {
    final Set<Primary> negated = conjunct.operands().stream()
            .map(Primary::negated).collect(Collectors.toSet());

    final boolean hasPair = conjunct.operands().stream()
            .anyMatch(negated::contains);

    if (hasPair) {
      return new AndExpression<>(Literal.FALSE);
    } else {
      return conjunct;
    }
  }

  private static AndExpression<Primary> filteredNeutral(final AndExpression<Primary> conjunct) {
    if (conjunct.operands().size() > 1) {
      return new AndExpression<>(conjunct.operands().stream()
              .filter(pr -> !pr.equals(Literal.TRUE)).collect(Collectors.toSet()));
    } else {
      return conjunct;
    }
  }

  private static AndExpression<Primary> falsified(final AndExpression<Primary> conjunct) {
    if (conjunct.operands().contains(Literal.FALSE)) {
      return new AndExpression<>(Literal.FALSE);
    } else {
      return conjunct;
    }
  }
}
