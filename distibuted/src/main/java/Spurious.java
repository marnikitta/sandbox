import java.util.concurrent.TimeUnit;

public final class Spurious {

  public static void main(final String... args) {
    new Spurious().run();
  }

  public void run() {
    final Thread a = new Wakeuper();
    final Thread b = new Interrupter(a);

    a.start();
    b.start();
    //fuck, forgot that java throws an exception when interrupted
  }
}

class Wakeuper extends Thread {
  private final Object sync = new Object();

  @Override
  public void run() {
    synchronized (this.sync) {
      try {
        this.sync.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class Interrupter extends Thread {
  private final Thread t;

  Interrupter(Thread t) {
    this.t = t;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(5));
      this.t.interrupt();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
