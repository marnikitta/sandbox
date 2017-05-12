package org.jetbrains.test;

import java.io.IOException;
import java.nio.file.Paths;

public final class DumpReaderMain {
  public static void main(final String... args) throws IOException, ClassNotFoundException {
    new DumpReaderMain().run();
  }
  public void run() throws IOException, ClassNotFoundException {
    final TreeCallTracer.MethodCall methodCall = TreeCallTracer.deserialize(Paths.get("dump"));
    System.out.println(methodCall);
  }
}
