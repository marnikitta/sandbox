package org.jetbrains.test;

import java.util.List;
import java.util.Random;

/**
 * Nikolay.Tropin
 * 18-Apr-17
 */
public class DummyApplication {
    private final List<String> args;
    private final TreeCallTracer tracer;
    private Random random = new Random(System.nanoTime());

    public DummyApplication(List<String> args, TreeCallTracer tracer) {
        this.tracer = tracer;
        this.args = args;
    }

    private boolean nextBoolean() {
        tracer.onEnter("nextBoolean");
        tracer.onExit();
        return random.nextBoolean();
    }

    private boolean stop() {
        tracer.onEnter("stop");
        tracer.onExit();
        return random.nextDouble() < 0.05;
    }

    private String nextArg() {
        tracer.onEnter("nextArg");
        int idx = random.nextInt(args.size());
        tracer.onExit();
        return args.get(idx);
    }

    private void sleep() {
        tracer.onEnter("sleep");
        try {
            Thread.sleep(random.nextInt(20));
        } catch (InterruptedException ignored) {

        }
        tracer.onExit();
    }

    private void abc(String s) {
        tracer.onEnter("abc", s);
        //your code here

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            def(nextArg());
        }
        else {
            xyz(nextArg());
        }
        tracer.onExit();
    }

    private void def(String s) {
        tracer.onEnter("def", s);

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            xyz(nextArg());
        }
        tracer.onExit();
    }

    private void xyz(String s) {
        tracer.onEnter("xyz", s);

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            def(nextArg());
        }
        tracer.onExit();
    }

    public void start() {
        tracer.onEnter("start");
        abc(nextArg());
        tracer.onExit();
    }
}
