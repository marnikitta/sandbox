package quoter;

import javax.annotation.PostConstruct;

@Profiling
@DeprecatedClass(newImpl = T100.class)
public class TerminatorQuoter implements Quoter {
  @InjectRandomInt(min = 2, max = 10)
  private int repeat;

  private final String quote;

  @PostConstruct
  public void postConstructor() {
    System.out.println("PostConstructor");
  }

  public TerminatorQuoter(String quote) {
    System.out.println("Constructor");
    this.quote = quote;
  }

  @Override
  @PostProxy
  public void sayQuote() {
    for (int i = 0; i < this.repeat; ++i) {
      System.out.println(this.quote);
    }
  }
}
