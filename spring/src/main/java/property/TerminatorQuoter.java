package property;

public final class TerminatorQuoter implements Quoter {
  private String name;

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void sayQuote() {
    System.out.println(name);
  }
}
