package mutal;

public final class PetersonNP implements Lock {
  private final int[] flag;
  private final int[] turn;
  private volatile long barrier;

  public PetersonNP(final int procCount) {
    this.flag = new int[procCount];
    this.turn = new int[procCount];
  }

  @Override
  public void lock(final int proc) {
    for (int k = 1; k < this.turn.length; ++k) {
      this.flag[proc] = k;
      this.turn[k] = proc;

      //Just linearization point. There is no logic here.
      this.barrier = proc;

      //Barrier is always greater than 0
      while (!this.tryRound(proc, k) && this.turn[k] == proc && this.barrier >= 0);
    }
  }

  private boolean tryRound(final int proc, final int k) {
    for (int i = 0; i < this.flag.length; i += 1) {
      if (proc != i && this.flag[i] >= k) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void unlock(final int proc) {
    this.flag[proc] = 0;
    this.barrier = proc;
  }
}
