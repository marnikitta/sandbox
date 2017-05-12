package ru.marnikitta.sandbox.codewars;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public final class JadenCase {
  public String toJadenCase(final String phrase) {
    return Optional.ofNullable(phrase).filter(p -> p.length() > 0)
            .map(p -> Arrays.stream(p.split(" "))
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                    .collect(Collectors.joining(" "))).orElse(null);
  }
}
