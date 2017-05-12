package org.stepik.marshalkin.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexLexer implements Lexer {

  private static final Pattern THE_PATTERN = Pattern.compile("\\s*(" +
          "(?<ID>[a-z]+)" +
          "|(?<LITERAL>TRUE|FALSE)" +
          "|(?<AND>AND)" +
          "|(?<OR>OR)" +
          "|(?<NOT>NOT)" +
          "|(?<LEFT>\\()" +
          "|(?<RIGHT>\\)))");

  @Override
  public List<Lexeme> lex(final CharSequence input) {
    final List<Lexeme> result = new ArrayList<>();

    int position = 0;
    final Matcher matcher = RegexLexer.THE_PATTERN.matcher(input);

    while (position < input.length()) {
      if (matcher.lookingAt()) {
        final LexemeClass lexemeClass = RegexLexer.extractClass(matcher);
        final CharSequence payload = input.subSequence(matcher.start(1), matcher.end());
        result.add(new Lexeme(payload, lexemeClass));

        position = matcher.end();
        matcher.region(position, input.length());
      } else {
        throw new IllegalArgumentException("Unexpected sequence: '" + input.subSequence(position, input.length()) + '\'');
      }
    }
    return result;
  }

  private static LexemeClass extractClass(final Matcher matcher) {
    for (final LexemeClass clazz : LexemeClass.values()) {
      final int end = matcher.end(clazz.name());
      if (end != -1) {
        return clazz;
      }
    }
    throw new IllegalStateException("At least one class should match");
  }
}
