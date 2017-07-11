package property;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

public final class PropertyFileApplicationContext extends GenericApplicationContext {
  public PropertyFileApplicationContext(String fileName) {
    final PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(this);
    final int i = reader.loadBeanDefinitions(fileName);
    System.out.println("Found " + i + " beans");
    this.refresh();
  }

  public static void main(String... args) {
    try (PropertyFileApplicationContext context = new PropertyFileApplicationContext("property/context.properties")) {
      context.getBean(Quoter.class).sayQuote();
    }
  }
}
