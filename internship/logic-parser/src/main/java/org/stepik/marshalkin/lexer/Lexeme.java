package org.stepik.marshalkin.lexer;

public final class Lexeme {
  private final CharSequence payload;

  private final LexemeClass clazz;

  public Lexeme(final CharSequence payload, final LexemeClass clazz) {
    this.payload = payload;
    this.clazz = clazz;
  }

  public CharSequence payload() {
    return this.payload;
  }

  public LexemeClass clazz() {
    return this.clazz;
  }

  @Override
  public String toString() {
    return "[" + this.payload + ':' + this.clazz + ']';
  }
}
