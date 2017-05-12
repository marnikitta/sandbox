package org.stepik.marshalkin.lexer;

import java.util.List;

public interface Lexer {
  List<Lexeme> lex(CharSequence input);
}
