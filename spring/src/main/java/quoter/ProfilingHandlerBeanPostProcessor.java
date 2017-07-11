package quoter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public final class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {
  private final Map<String, Class<?>> map = new HashMap<>();
  private final ProfilingController controller = new ProfilingController();

  public ProfilingHandlerBeanPostProcessor() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    server.registerMBean(this.controller, new ObjectName("profiling", "name", "controller"));
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    final Class<?> clazz = bean.getClass();
    if (clazz.isAnnotationPresent(Profiling.class)) {
      this.map.put(beanName, clazz);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    if (this.map.containsKey(beanName)) {
      final Class<?> beanClass = this.map.get(beanName);
      return Proxy.newProxyInstance(bean.getClass().getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          if (ProfilingHandlerBeanPostProcessor.this.controller.isEnabled()) {
            System.out.println("Ama profiling");
            final long start = System.nanoTime();
            final Object retval = method.invoke(bean, args);
            final long end = System.nanoTime() - start;
            System.out.println("Done: " + end);
            return retval;
          } else {
            return method.invoke(bean, args);
          }
        }
      });
    }
    return bean;
  }
}
