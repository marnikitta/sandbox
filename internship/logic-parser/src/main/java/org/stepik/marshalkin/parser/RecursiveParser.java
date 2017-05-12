package org.stepik.marshalkin.parser;

import org.stepik.marshalkin.AndExpression;
import org.stepik.marshalkin.Expression;
import org.stepik.marshalkin.Id;
import org.stepik.marshalkin.Literal;
import org.stepik.marshalkin.NotExpression;
import org.stepik.marshalkin.OrExpression;
import org.stepik.marshalkin.lexer.Lexeme;
import org.stepik.marshalkin.lexer.LexemeClass;

import java.util.List;

public final class RecursiveParser implements Parser {

  @Override
  public Expression parse(final List<Lexeme> input) {
    final RecursiveResult expression = RecursiveParser.tryExpression(input);

    if (expression.isMatched() && expression.end == input.size()) {
      return expression.payload;
    } else {
      throw new IllegalArgumentException("Unexpected input");
    }
  }

  private static RecursiveResult tryExpression(final List<Lexeme> input) {
    final RecursiveResult orExpression = RecursiveParser.tryOrExpression(input);
    if (orExpression.isMatched()) {
      return orExpression;
    } else {
      return RecursiveParser.tryTerm(input);
    }
  }

  private static RecursiveResult tryOrExpression(final List<Lexeme> input) {
    final RecursiveResult term = RecursiveParser.tryTerm(input);
    if (term.isMatched()) {
      final int firstOperandEnd = term.end;
      final boolean hasOr = input.size() > firstOperandEnd && input.get(firstOperandEnd).clazz() == LexemeClass.OR;

      if (hasOr) {
        final RecursiveResult exp = RecursiveParser.tryExpression(input.subList(firstOperandEnd + 1, input.size()));

        if (exp.isMatched()) {
          return new RecursiveResult(new OrExpression<>(term.payload, exp.payload), firstOperandEnd + 1 + exp.end);
        }
      }
    }

    return RecursiveResult.NOT_MATCHED;
  }

  private static RecursiveResult tryTerm(final List<Lexeme> input) {
    final RecursiveResult andTerm = RecursiveParser.tryAndTerm(input);

    if (andTerm.isMatched()) {
      return andTerm;
    } else {
      return RecursiveParser.tryFactor(input);
    }
  }

  private static RecursiveResult tryAndTerm(final List<Lexeme> input) {
    final RecursiveResult factor = RecursiveParser.tryFactor(input);

    if (factor.isMatched()) {
      final int firstOperandEnd = factor.end;
      final boolean hasAnd = input.size() > firstOperandEnd && input.get(firstOperandEnd).clazz() == LexemeClass.AND;

      if (hasAnd) {
        final RecursiveResult term = RecursiveParser.tryTerm(input.subList(firstOperandEnd + 1, input.size()));

        if (term.isMatched()) {
          return new RecursiveResult(new AndExpression<>(factor.payload, term.payload), firstOperandEnd + 1 + term.end);
        }
      }
    }
    return RecursiveResult.NOT_MATCHED;
  }

  private static RecursiveResult tryFactor(final List<Lexeme> input) {
    final boolean hasNot = input.size() > 1 && input.get(0).clazz() == LexemeClass.NOT;
    final int start = hasNot ? 1 : 0;
    final RecursiveResult primary = RecursiveParser.tryPrimary(input.subList(start, input.size()));

    if (primary.isMatched() && hasNot) {
      return new RecursiveResult(new NotExpression<>(primary.payload), start + primary.end);
    } else {
      return primary;
    }
  }

  private static RecursiveResult tryPrimary(final List<Lexeme> input) {
    final RecursiveResult literal = RecursiveParser.tryLiteral(input);

    if (literal.isMatched()) {
      return literal;
    } else {
      final RecursiveResult id = RecursiveParser.tryId(input);
      if (id.isMatched()) {
        return id;
      } else {
        return RecursiveParser.tryBracketed(input);
      }
    }
  }

  private static RecursiveResult tryLiteral(final List<Lexeme> input) {
    if (!input.isEmpty() && input.get(0).clazz() == LexemeClass.LITERAL) {
      return new RecursiveResult(Literal.valueOf(input.get(0).payload().toString()), 1);
    } else {
      return RecursiveResult.NOT_MATCHED;
    }
  }

  private static RecursiveResult tryId(final List<Lexeme> input) {
    if (!input.isEmpty() && input.get(0).clazz() == LexemeClass.ID) {
      return new RecursiveResult(new Id(input.get(0).payload().toString()), 1);
    } else {
      return RecursiveResult.NOT_MATCHED;
    }
  }

  private static RecursiveResult tryBracketed(final List<Lexeme> input) {
    if (!input.isEmpty() && input.get(0).clazz() == LexemeClass.LEFT) {
      final RecursiveResult exp = RecursiveParser.tryExpression(input.subList(1, input.size()));
      if (exp.isMatched()) {
        final int absoluteEnd = 1 + exp.end;

        if (input.size() > absoluteEnd && input.get(absoluteEnd).clazz() == LexemeClass.RIGHT) {
          return new RecursiveResult(exp.payload, absoluteEnd + 1);
        }
      }
    }
    return RecursiveResult.NOT_MATCHED;
  }

  private static class RecursiveResult {
    static final RecursiveResult NOT_MATCHED = new RecursiveResult(null, -1) {
      @Override
      public boolean isMatched() {
        return false;
      }
    };

    final Expression payload;

    final int end;


    RecursiveResult(final Expression payload, final int end) {
      this.payload = payload;
      this.end = end;
    }

    boolean isMatched() {
      return true;
    }
  }
}
