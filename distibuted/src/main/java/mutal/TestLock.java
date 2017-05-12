package mutal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TestLock {
  private volatile double shared = 0;

  public static void main(final String... args) {
    //new TestLock().testLock(new PetersonNP(10), 10);
    //new TestLock().testLock(new DijkstraME(10), 10);
    new TestLock().testLock(new Bakery(10), 10);
  }

  public void testLock(final Lock lock, final int procCount) {
    final ExecutorService service = Executors.newFixedThreadPool(procCount);

    for (int i = 0; i < procCount; ++i) {
      service.submit(new Runnalka(lock, i));
    }
    service.shutdown();
  }

  private final class Runnalka implements Runnable {
    private final Lock lock;
    private final int id;

    private Runnalka(final Lock lock, final int id) {
      this.id = id;
      this.lock = lock;
    }

    @Override
    public void run() {
      try {
        for (int i = 0; i < 1000000; i++) {
          this.lock.lock(this.id);
          TestLock.this.shared += 1;
          TestLock.this.shared -= 1;
          System.out.println("In " + id);
          if (TestLock.this.shared != 0.0d) {
            System.out.println("WTF " + TestLock.this.shared);
            this.lock.unlock(this.id);
            break;
          } else {
            this.lock.unlock(this.id);
          }
        }
        System.out.println("Done " + this.id);
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }
}
