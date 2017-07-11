package quoter;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {
  @Autowired
  private ConfigurableListableBeanFactory beanFactory;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    final ApplicationContext context = contextRefreshedEvent.getApplicationContext();
    for (String name : context.getBeanDefinitionNames()) {
      final BeanDefinition definition = this.beanFactory.getBeanDefinition(name);
      final String originalClassName = definition.getBeanClassName();
      try {
        final Class<?> originalClass = Class.forName(originalClassName);
        for (Method method : originalClass.getDeclaredMethods()) {
          if (method.isAnnotationPresent(PostProxy.class)) {
            final Object bean = context.getBean(name);
            final Method realMethod = bean.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            realMethod.invoke(bean);
          }
        }
      } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
