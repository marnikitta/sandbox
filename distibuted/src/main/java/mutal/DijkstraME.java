package mutal;

public final class DijkstraME implements Lock {
  private final int[] flag;
  private volatile int turn;

  public DijkstraME(final int procCount) {
    this.turn = 0;
    this.flag = new int[procCount];
  }

  @Override
  public void lock(final int proc) {
    boolean win = false;
    while (!win) {
      this.flag[proc] = 1;

      while (this.turn != proc) {
        if (this.flag[this.turn] == 0) {
          this.turn = proc;
        }
      }

      this.flag[proc] = 2;
      //Linearization point
      //noinspection NonAtomicOperationOnVolatileField,SillyAssignment
      this.turn = this.turn;

      win = true;
      for (int i = 0; i < this.flag.length; i++) {
        if (i != proc && this.flag[i] == 2) {
          win = false;
        }
      }
    }
  }

  @Override
  public void unlock(final int proc) {
    this.flag[proc] = 0;
  }
}
