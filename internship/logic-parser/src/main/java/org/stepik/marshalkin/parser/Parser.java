package org.stepik.marshalkin.parser;

import org.stepik.marshalkin.Expression;
import org.stepik.marshalkin.lexer.Lexeme;

import java.util.List;

public interface Parser {
  Expression parse(List<Lexeme> input);
}
