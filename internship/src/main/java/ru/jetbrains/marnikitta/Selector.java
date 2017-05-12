package ru.jetbrains.marnikitta;

public interface Selector {

  /**
   * Extract data (attributes, html, text, etc) from WebElement.
   */
  SelectorResult apply(WebElement elem);

}