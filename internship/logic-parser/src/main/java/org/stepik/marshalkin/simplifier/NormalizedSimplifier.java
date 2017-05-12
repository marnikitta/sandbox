package org.stepik.marshalkin.simplifier;

import org.stepik.marshalkin.AndExpression;
import org.stepik.marshalkin.OrExpression;
import org.stepik.marshalkin.Primary;

import java.util.function.Function;
import java.util.stream.Collectors;

public final class NormalizedSimplifier implements Function<OrExpression<AndExpression<Primary>>, OrExpression<AndExpression<Primary>>> {
  private final OrSimplifier orSimplifier = new OrSimplifier();
  private final AndSimplifier andSimplifier = new AndSimplifier();

  @Override
  public OrExpression<AndExpression<Primary>> apply(final OrExpression<AndExpression<Primary>> disjunct) {

    return this.orSimplifier.apply(new OrExpression<>(
            disjunct.operands().stream().map(this.andSimplifier).collect(Collectors.toSet())
    ));
  }
}
