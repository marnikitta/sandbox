package mutal;

public interface Lock {
  void lock(int proc);
  void unlock(int proc);
}
