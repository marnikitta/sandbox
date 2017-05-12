package ru.jetbrains.marnikitta;

public class CustomFuture<E> {
  private E result;
  private volatile boolean ready;
  private final Object waitingPoint = new Object();

  public CustomFuture() {
    this.result = null;
    this.ready = false;
  }


  /*** Proof-sketch
   *
   * PO = Program Order relation
   * SO = Synchronization Order relation
   * HB = Happens Before relation
   *
   * 1. PO(write.1, write.2) => HB(write.1, write.2)
   * 2. this.read - volatile => SO(write.2, read.1) =>
   *    => HB(write.2, read.1)
   * 3. PO(read.1, read.2) => HB(read.1, read.2)
   * 1 + 2 + 3 => (this.ready == true) => (this.result != default null)
   */

  public CustomFuture(E result) {
    //write.1
    this.result = result;
    //write.2
    this.ready = true;
  }

  public void setResult(E result) {
    //write.1
    this.result = result;
    //write.2
    this.ready = true;

    synchronized (this.waitingPoint) {
      this.waitingPoint.notifyAll();
    }
  }

  public E waitResult() throws InterruptedException {

    //read.1
    while (!this.ready) {
      synchronized (this.waitingPoint) {
        this.waitingPoint.wait();
      }
    }

    //read.2
    return this.result;
  }

  public boolean isFinished() {
    return this.ready;
  }
}
