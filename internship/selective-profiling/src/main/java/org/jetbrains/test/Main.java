package org.jetbrains.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

  public static void main(String[] args) throws InterruptedException, IOException {
    List<TreeCallTracer> tracers = new ArrayList<>();
    ExecutorService service = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 5; i++) {
      int start = 100 * i;
      TreeCallTracer tracer = new TreeCallTracer();
      tracers.add(tracer);
      List<String> arguments = IntStream.range(start, start + 10)
              .mapToObj(Integer::toString)
              .collect(Collectors.toList());
      service.submit(() -> new DummyApplication(arguments, tracer).start());
    }
    Thread.sleep(100);
    service.shutdown();

    tracers.forEach(System.out::println);

    final TreeCallTracer.MethodCall firstRoot = tracers.stream().findFirst()
            .orElseThrow(NoSuchElementException::new).root();

    TreeCallTracer.serialize(firstRoot, Paths.get("dump"));
  }
}
