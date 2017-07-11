package quoter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

public final class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    final Field[] declaredFields = bean.getClass().getDeclaredFields();
    for (Field field : declaredFields) {
      if (field.isAnnotationPresent(InjectRandomInt.class)) {
        final InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
        final int value = new Random().nextInt(annotation.max() - annotation.min()) + annotation.min();
        field.setAccessible(true);
        ReflectionUtils.setField(field, bean, value);
      }
    }

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
