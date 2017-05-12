package org.stepik.marshalkin.simplifier;

import org.stepik.marshalkin.AndExpression;
import org.stepik.marshalkin.Literal;
import org.stepik.marshalkin.OrExpression;
import org.stepik.marshalkin.Primary;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OrSimplifier implements Function<OrExpression<AndExpression<Primary>>, OrExpression<AndExpression<Primary>>> {

  private static final AndExpression<Primary> TRUE = new AndExpression<>(Literal.TRUE);
  private static final AndExpression<Primary> FALSE = new AndExpression<>(Literal.FALSE);

  @Override
  public OrExpression<AndExpression<Primary>> apply(final OrExpression<AndExpression<Primary>> disjunct) {
    return OrSimplifier.collapsedNegates(OrSimplifier.filteredNeutral(OrSimplifier.posified(disjunct)));
  }

  private static OrExpression<AndExpression<Primary>> collapsedNegates(final OrExpression<AndExpression<Primary>> disjunct) {
    final Set<AndExpression<Primary>> negatedPrimaries = disjunct.operands().stream()
            .map(AndExpression::operands)
            .filter(op -> op.size() == 1)
            .flatMap(Collection::stream)
            .map(Primary::negated)
            .map(AndExpression::new)
            .collect(Collectors.toSet());

    final boolean hasPair = disjunct.operands().stream()
            .anyMatch(negatedPrimaries::contains);

    if (hasPair) {
      return new OrExpression<>(OrSimplifier.TRUE);
    } else {
      return disjunct;
    }
  }

  private static OrExpression<AndExpression<Primary>> filteredNeutral(final OrExpression<AndExpression<Primary>> disjunct) {
    if (disjunct.operands().size() > 1) {
      return new OrExpression<>(disjunct.operands().stream()
              .filter(pr -> !pr.equals(OrSimplifier.FALSE)).collect(Collectors.toSet()));
    } else {
      return disjunct;
    }
  }

  private static OrExpression<AndExpression<Primary>> posified(final OrExpression<AndExpression<Primary>> disjunct) {
    if (disjunct.operands().contains(OrSimplifier.TRUE)) {
      return new OrExpression<>(OrSimplifier.TRUE);
    } else {
      return disjunct;
    }
  }
}
