package org.stepik.marshalkin;

public enum Literal implements Primary {
  TRUE {
    @Override
    public Primary negated() {
      return Literal.FALSE;
    }
  },
  FALSE {
    @Override
    public Primary negated() {
      return Literal.TRUE;
    }
  };

  @Override
  public OrExpression<AndExpression<Primary>> normalized() {
    return new OrExpression<>(new AndExpression<>(this));
  }
}
