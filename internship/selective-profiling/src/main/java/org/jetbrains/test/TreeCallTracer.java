package org.jetbrains.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TreeCallTracer implements CallTracker {
  private final MethodCall root = new MethodCall("META_CALL", new Object[0]);
  private MethodCall parentCall = this.root;
  private MethodCall currentCall = this.root;

  @Override
  public void onEnter(final String methodName, final Object... args) {
    final MethodCall methodCall = new MethodCall(methodName, args);

    this.currentCall.addSubCall(methodCall);
    this.parentCall = this.currentCall;
    this.currentCall = methodCall;
  }

  @Override
  @SuppressWarnings("ObjectEquality")
  public void onExit() {
    if (this.parentCall == this.currentCall) {
      throw new IllegalStateException("Unmatched onExit call");
    } else {
      this.currentCall = this.parentCall;
    }
  }

  public MethodCall currentCall() {
    return this.currentCall;
  }

  public MethodCall root() {
    return this.root;
  }

  public static void serialize(final MethodCall root, final Path out) throws IOException {
    final OutputStream os = Files.newOutputStream(out, StandardOpenOption.TRUNCATE_EXISTING);
    try (final ObjectOutput oo = new ObjectOutputStream(os)) {
      oo.writeObject(root);
    }
  }

  public static MethodCall deserialize(final Path in) throws IOException, ClassNotFoundException {
    final InputStream is = Files.newInputStream(in, StandardOpenOption.READ);
    try (final ObjectInput oi = new ObjectInputStream(is)) {
      return (MethodCall) oi.readObject();
    }
  }

  @Override
  public String toString() {
    return this.root.toString();
  }

  public static final class MethodCall implements Serializable {
    private static final long serialVersionUID = -1885698857607426737L;

    private final String methodName;
    private final List<Object> args;
    private final List<MethodCall> subCalls = new ArrayList<>();

    private MethodCall(final String methodName,
                       final Object[] args) {
      this.methodName = methodName;
      this.args = Arrays.asList(args);
    }

    public List<Object> args() {
      return Collections.unmodifiableList(this.args);
    }

    public String methodName() {
      return this.methodName;
    }

    public List<MethodCall> subCalls() {
      return Collections.unmodifiableList(this.subCalls);
    }

    private String toString(final int indentSize) {
      final String indent = Stream.generate(() -> " ").limit(indentSize).collect(Collectors.joining());

      final String header = indent + this.methodName +
              this.args.stream().map(Object::toString)
                      .collect(Collectors.joining(",  ", "(", ")"));

      final String subTree = this.subCalls.stream()
              .map(call -> call.toString(indentSize + 2))
              .collect(Collectors.joining('\n' + indent, indent, ""));

      return header + (this.subCalls.isEmpty() ? "" : '\n' + subTree);
    }

    private void addSubCall(final MethodCall call) {
      this.subCalls.add(call);
    }

    @Override
    public String toString() {
      return this.toString(0);
    }
  }
}
