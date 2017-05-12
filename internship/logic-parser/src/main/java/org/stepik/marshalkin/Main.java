package org.stepik.marshalkin;

import org.stepik.marshalkin.lexer.Lexer;
import org.stepik.marshalkin.lexer.RegexLexer;
import org.stepik.marshalkin.parser.Parser;
import org.stepik.marshalkin.parser.RecursiveParser;
import org.stepik.marshalkin.simplifier.NormalizedSimplifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Main {
  private final Lexer lexer = new RegexLexer();
  private final Parser parser = new RecursiveParser();
  private final NormalizedSimplifier simplifier = new NormalizedSimplifier();

  public static void main(final String... args) throws IOException {
    new Main().run();
  }

  public void run() throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      boolean alive = true;

      while (alive) {
        try {
          System.out.print("$ ");
          final String input = br.readLine();

          if (input != null && !"quit".equals(input)) {
            System.out.println(this.simplifier.apply(this.parser.parse(this.lexer.lex(input)).normalized()));
          } else {
            alive = false;
          }
        } catch (final IllegalArgumentException e) {
          System.err.println(e.getMessage());
        }
      }
    }
  }
}
