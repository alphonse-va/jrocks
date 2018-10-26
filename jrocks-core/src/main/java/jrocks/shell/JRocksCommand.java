package jrocks.shell;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that an annotated class may contain shell methods (themselves annotated with {@link JRocksShellMethod}) that
 * is,
 * methods that may be invoked reflectively by the shell.
 *
 * <p>This annotation is a specialization of {@link Component}.</p>
 *
 * @author Eric Bottard
 * @see Component
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface JRocksCommand {

  /**
   * The default value for {@link #group()}, meaning that the group will be inherited from the explicit value set
   * on the containing element (class then package) or ultimately inferred.
   */
  String INHERITED = "";

  /**
   * The name(s) by which this method can be invoked via Spring Shell. If not specified, the actual method name
   * will be used (turning camelCase humps into "-").
   *
   * @return explicit command name(s) to use
   */
  String[] key() default {};

  /**
   * A description for the command. Should not contain any formatting (e.g. html) characters and would typically
   * start with a capital letter and end with a dot.
   *
   * @return short description of what the command does
   */
  String value() default "";

  /**
   * The prefix to use for assigning parameters by name.
   *
   * @return prefix to use when not specified as part of the parameter annotation
   */
  String prefix() default "--";

  /**
   * The command group which this command belongs to. The command group is used when printing a list of
   * commands to group related commands. By default, group is first looked up from owning class then package,
   * and if not explicitly set, is inferred from class name.
   *
   * @return name of the command group
   */
  String group() default INHERITED;
}
