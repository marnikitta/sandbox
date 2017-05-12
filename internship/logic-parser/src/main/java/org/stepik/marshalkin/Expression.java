package org.stepik.marshalkin;

public interface Expression {
  OrExpression<AndExpression<Primary>> normalized();
}
