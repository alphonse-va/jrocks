package jrocks.shell;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.shell.*;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.StandardMethodTargetRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.collectionToDelimitedString;

@Component
public class JrocksMethodTargetRegistrar extends StandardMethodTargetRegistrar {

  @Autowired
  private ListableBeanFactory applicationContext;


  private Map<String, MethodTarget> commands = new HashMap<>();

  @Override
  public void register(ConfigurableCommandRegistry registry) {
    Map<String, Object> commandBeans = applicationContext.getBeansWithAnnotation(JRocksCommand.class);
    for (Object bean : commandBeans.values()) {
      Class<?> clazz = bean.getClass();
      JRocksCommand shellMapping = clazz.getAnnotation(JRocksCommand.class);
      ReflectionUtils.doWithMethods(clazz, method -> {
        String[] keys = shellMapping.key();
        if (keys.length == 0) {
          keys = new String[]{Utils.unCamelify(method.getName())};
        }
        String group = getOrInferGroup(method, clazz);
        for (String key : keys) {
          Supplier<Availability> availabilityIndicator = findAvailabilityIndicator(keys, bean, method);
          MethodTarget target = new MethodTarget(method, bean, new Command.Help(shellMapping.value(), group), availabilityIndicator);
          registry.register(key, target);
          commands.put(key, target);
        }
      }, method -> method.getAnnotation(JRocksShellMethod.class) != null);
    }
  }

  /**
   * Gets the group from the following places, in order:<ul>
   * <li>explicit annotation at the method level</li>
   * <li>explicit annotation at the class level</li>
   * <li>explicit annotation at the package level</li>
   * <li>implicit from the class name</li>
   * </ul>
   */
  private String getOrInferGroup(Method method, Class<?> jrocksCommandClass) {
    final JRocksCommand jrocksClassAnn = jrocksCommandClass.getAnnotation(JRocksCommand.class);
    if (!jrocksClassAnn.group().equals(ShellMethod.INHERITED)) {
      return jrocksClassAnn.group();
    }
    Class<?> clazz = method.getDeclaringClass();
    ShellCommandGroup classAnn = AnnotationUtils.getAnnotation(clazz, ShellCommandGroup.class);
    if (classAnn != null && !classAnn.value().equals(ShellCommandGroup.INHERIT_AND_INFER)) {
      return classAnn.value();
    }
    ShellCommandGroup packageAnn = AnnotationUtils.getAnnotation(clazz.getPackage(), ShellCommandGroup.class);
    if (packageAnn != null && !packageAnn.value().equals(ShellCommandGroup.INHERIT_AND_INFER)) {
      return packageAnn.value();
    }
    // Shameful copy/paste from https://stackoverflow.com/questions/7593969/regex-to-split-camelcase-or-titlecase-advanced
    return StringUtils.arrayToDelimitedString(clazz.getSimpleName().split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"), " ");
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
    final Method indicator;
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
