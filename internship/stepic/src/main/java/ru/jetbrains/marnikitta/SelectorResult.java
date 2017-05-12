package ru.jetbrains.marnikitta;

import java.util.List;

public interface SelectorResult {

  /**
   * Return data (attributes, html, text, etc) given name of Selector.
   * If there is no data for such name then empty list is returned.
   */
  List<String> getFieldsBySelectorName(String name);

}