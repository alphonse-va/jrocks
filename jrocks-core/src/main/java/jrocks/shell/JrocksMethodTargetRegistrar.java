package jrocks.shell;

import jrocks.plugin.api.JRocksPlugin;
import jrocks.shell.command.ClassGeneratorCommand;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.*;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.collectionToDelimitedString;

@Component
public class JrocksMethodTargetRegistrar implements MethodTargetRegistrar {

  private final ListableBeanFactory applicationContext;

  private Map<String, MethodTarget> commands = new HashMap<>();

  @Autowired
  public JrocksMethodTargetRegistrar(ListableBeanFactory applicationContext) {this.applicationContext = applicationContext;}

  @Override
  public void register(ConfigurableCommandRegistry registry) {
    Map<String, JRocksPlugin> plugins = applicationContext.getBeansOfType(JRocksPlugin.class);

    ClassGeneratorCommand generatorCommand = applicationContext.getBean(ClassGeneratorCommand.class);
    Method method = findJRocksShellMethod(generatorCommand.getClass());
    for (JRocksPlugin plugin : plugins.values()) {
      plugin.keys().forEach(key -> {
        Supplier<Availability> availabilityIndicator = findAvailabilityIndicator(plugin.keys().toArray(new String[0]), generatorCommand, method);
        MethodTarget target = new MethodTarget(method, generatorCommand, new Command.Help(plugin.description(), plugin.group()), availabilityIndicator);
        registry.register(key, target);
        commands.put(key, target);
      });
    }
  }

  private static Method findJRocksShellMethod(Class<?> clazz) {
    for (Method method : clazz.getMethods())
      if (method.isAnnotationPresent(JRocksShellMethod.class))
        return (method);
    return (null);
  }

  /**
   * Tries to locate an availability indicator (a no-arg method that returns
   * {@link Availability}) for the given command method. The following are tried in order
   * for method {@literal m}:
   * <ol>
   * <li>If {@literal m} bears the {@literal @}{@link ShellMethodAvailability} annotation,
   * its value should be the method name to look up</li>
   * <li>a method named {@literal "<m>Availability"} is looked up.</li>
   * <li>otherwise, if some method {@literal ai} that returns {@link Availability} and takes
   * no argument exists, that is annotated with {@literal @}{@link ShellMethodAvailability}
   * and whose annotation value contains one of the {@literal commandKeys}, then it is
   * selected</li>
   * </ol>
   */
  private Supplier<Availability> findAvailabilityIndicator(String[] commandKeys, Object bean, Method method) {
    ShellMethodAvailability explicit = method.getAnnotation(ShellMethodAvailability.class);
    Method indicator;
    if (explicit != null) {
      Assert.isTrue(explicit.value().length == 1, "When set on a @" +
          ShellMethod.class.getSimpleName() + " method, the value of the @"
          + ShellMethodAvailability.class.getSimpleName() +
          " should be a single element, the name of a method that returns "
          + Availability.class.getSimpleName() +
          ". Found " + Arrays.asList(explicit.value()) + " for " + method);
      indicator = ReflectionUtils.findMethod(bean.getClass(), explicit.value()[0]);
    } // Try "<method>Availability"
    else {
      Method implicit = ReflectionUtils.findMethod(bean.getClass(), method.getName() + "Availability");
      if (implicit != null) {
        indicator = implicit;
      } else {
        Map<Method, Collection<String>> candidates = new HashMap<>();
        ReflectionUtils.doWithMethods(bean.getClass(), candidate -> {
          List<String> matchKeys = new ArrayList<>(Arrays.asList(candidate.getAnnotation(ShellMethodAvailability.class).value()));
          if (matchKeys.contains("*")) {
            Assert.isTrue(matchKeys.size() == 1, "When using '*' as a wildcard for " +
                ShellMethodAvailability.class.getSimpleName() + ", this can be the only value. Found " +
                matchKeys + " on method " + candidate);
            candidates.put(candidate, matchKeys);
          } else {
            matchKeys.retainAll(Arrays.asList(commandKeys));
            if (!matchKeys.isEmpty()) {
              candidates.put(candidate, matchKeys);
            }
          }
        }, m -> m.getAnnotation(ShellMethodAvailability.class) != null && m.getAnnotation(ShellMethod.class) == null);

        // Make sure wildcard approach has less precedence than explicit name
        Set<Method> notUsingWildcard = candidates.entrySet().stream()
            .filter(e -> !e.getValue().contains("*"))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());

        Assert.isTrue(notUsingWildcard.size() <= 1,
            "Found several @" + ShellMethodAvailability.class.getSimpleName() +
                " annotated methods that could apply for " + method + ". Offending candidates are "
                + notUsingWildcard);

        if (notUsingWildcard.size() == 1) {
          indicator = notUsingWildcard.iterator().next();
        } // Wildcard was available
        else if (candidates.size() == 1) {
          indicator = candidates.keySet().iterator().next();
        } else {
          indicator = null;
        }
      }
    }

    if (indicator != null) {
      Assert.isTrue(indicator.getReturnType().equals(Availability.class),
          "Method " + indicator + " should return " + Availability.class.getSimpleName());
      Assert.isTrue(indicator.getParameterCount() == 0, "Method " + indicator + " should be a no-arg method");
      ReflectionUtils.makeAccessible(indicator);
      return () -> (Availability) ReflectionUtils.invokeMethod(indicator, bean);
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " contributing "
        + collectionToDelimitedString(commands.keySet(), ", ", "[", "]");
  }
}
