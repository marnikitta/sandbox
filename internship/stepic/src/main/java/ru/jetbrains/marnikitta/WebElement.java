package ru.jetbrains.marnikitta;

import java.util.List;

public interface WebElement {

  /**
   * Get the value of a the given attribute of the element. If no value is set, null is returned.
   * This method can also be used to get HTML of element by providing name parameter as "outerHTML"
   */
  String getAttribute(String name);


  /**
   * Get the visible (i.e. not hidden by CSS) innerText of this element, including sub-elements,
   * without any leading or trailing whitespace.
   */
  String getText();

  /**
   * Find all elements within the current context using the given css selector.
   */
  List<WebElement> findElements(String cssSelector);

  /**
   * Find the first {@link WebElement} using the given method. See the note in
   * {@link #findElements(By)} about finding via XPath.
   */
  WebElement findElement(String cssSelector);


}