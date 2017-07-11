package quoter;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

public class TerminatorQuoterTest {
  @Test
  public void testContext() throws Exception {
    try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("quoter/context.xml")) {
      ctx.getBean(Quoter.class).sayQuote();
    }
  }
}