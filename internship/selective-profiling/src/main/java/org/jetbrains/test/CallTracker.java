package org.jetbrains.test;

public interface CallTracker {
  void onEnter(String methodName, Object... args);

  void onExit();
}
