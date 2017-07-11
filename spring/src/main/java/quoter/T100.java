package quoter;

public final class T100 extends TerminatorQuoter implements Quoter {
  public T100(String quote) {
    super(quote);
  }

  @Override
  public void sayQuote() {
    System.out.println("Ama liquid");
  }
}
