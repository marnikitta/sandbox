package mutal;

public final class Bakery implements Lock {
  private final int[] choosing;
  private final long[] number;
  private volatile int barrier;

  public Bakery(final int procCount) {
    this.choosing = new int[procCount];
    this.number = new long[procCount];
  }

  @Override
  public void lock(final int proc) {
    this.choosing[proc] = 1;
    this.barrier = 0;

    this.number[proc] = Bakery.max(this.number) + 1;
    this.barrier = barrier;

    this.choosing[proc] = 0;
    this.barrier = barrier;

    for (int i = 0; i < this.choosing.length && this.barrier >= 0; ++i) {
      if (i != proc) {
        while (this.choosing[i] == 1) ;
        while (!this.canContinue(proc, i)) ;
      }
    }
  }

  private boolean canContinue(final int proc, final int otherProc) {
    return this.number[otherProc] == 0
            || this.number[proc] < this.number[otherProc]
            || this.number[proc] == this.number[otherProc] && proc < otherProc;
  }

  private static long max(final long[] numbers) {
    long max = -1;
    for (final long number : numbers) {
      max = Math.max(number, max);
    }
    return max;
  }

  @Override
  public void unlock(final int proc) {
    this.number[proc] = 0;
    this.barrier = proc;
  }
}
