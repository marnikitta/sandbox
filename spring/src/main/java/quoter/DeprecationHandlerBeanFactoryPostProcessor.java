package quoter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public final class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    for (String name : beanFactory.getBeanDefinitionNames()) {
      final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
      final String beanClassName = beanDefinition.getBeanClassName();
      try {
        final Class<?> clazz = Class.forName(beanClassName);
        if (clazz.isAnnotationPresent(DeprecatedClass.class)) {
          beanDefinition.setBeanClassName(clazz.getAnnotation(DeprecatedClass.class).newImpl().getCanonicalName());
        }

      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
