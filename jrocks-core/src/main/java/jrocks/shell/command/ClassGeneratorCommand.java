package jrocks.shell.command;

import jrocks.model.ClassInfoBuilder;
import jrocks.model.ClassInfoParameter;
import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.GeneratedSource;
import jrocks.plugin.api.JRocksPlugin;
import jrocks.plugin.api.PluginLayout;
import jrocks.shell.ClassPathScanner;
import jrocks.shell.JRocksShellMethod;
import jrocks.shell.autocomplete.AdditionalFlagValueProvider;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import jrocks.shell.autocomplete.LayoutValueProvider;
import jrocks.shell.generator.TemplateWriterService;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@SuppressWarnings("WeakerAccess")
@Component
public class ClassGeneratorCommand extends BaseCommand {

  public static final String PARAM_CLASS = "--class";
  public static final String PARAM_SUFFIX_TO_REMOVE = "--defaultSuffix-to-remove";
  public static final String PARAM_EXCLUDED_FIELDS = "--excluded-fields";
  public static final String PARAM_INCLUDED_FIELDS = "--included-fields";
  public static final String PARAM_MANDATORY_FIELDS = "--mandatory-fields";
  public static final String PARAM_ADDITIONAL_FLAGS = "--additional-flags";
  public static final String PARAM_FORCE = "--force";
  private static final String PARAM_SUFFIX = "--defaultSuffix";
  private static final String PARAM_LAYOUT = "--layout";

  private ClassPathScanner classPathScanner;

  @Autowired
  private TemplateWriterService writerService;

  @Autowired
  private CurrentPluginHolder currentPluginHolder;

  @ShellMethod
  @JRocksShellMethod
  public void generator(
      @ShellOption(value = PARAM_CLASS, help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = PARAM_SUFFIX, help = "Suffix to remove", defaultValue = "") String suffixToRemove,
      @ShellOption(value = PARAM_SUFFIX_TO_REMOVE, help = "Suffix to remove", defaultValue = "") String suffix,

      @ShellOption(value = PARAM_EXCLUDED_FIELDS, help = "Fields to exclude", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = PARAM_INCLUDED_FIELDS, help = "Fields to include", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = PARAM_MANDATORY_FIELDS, help = "Mandatory fields", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = PARAM_ADDITIONAL_FLAGS, help = "Generator additional flags", defaultValue = "[]", valueProvider = AdditionalFlagValueProvider.class) String[] additionalFlags,
      @ShellOption(value = PARAM_LAYOUT, help = "Template layout", defaultValue = "", valueProvider = LayoutValueProvider.class) String layout,
      @ShellOption(value = PARAM_FORCE, help = "Overwrite existing files") boolean isForced) {

//    LineReader reader = LineReaderBuilder
//        .builder()
//        .highlighter((reader1, buffer) -> new AttributedString(buffer, AttributedStyle.BOLD.foreground(AttributedStyle.WHITE)))
//        .completer((reader1, line, candidates) -> {
//          candidates.add(new Candidate("yes"));
//          candidates.add(new Candidate("no"));
//        }).build();
//    reader.unsetOpt(LineReader.Option.INSERT_TAB);
//    String prompt = new AttributedStringBuilder()
//        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
//        .append("- Do you want to fuck?")
//        .style(AttributedStyle.DEFAULT)
//        .append(" yes/no : ")
//        .toAnsi();
//    String rightPrompt = new AttributedStringBuilder()
//        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
//        .append("(")
//        .append(plugin.description())
//        .append(") ")
//        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
//        .append(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
//        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE | AttributedStyle.BRIGHT))
//        .append(LocalTime.now().format(new DateTimeFormatterBuilder()
//            .appendValue(HOUR_OF_DAY, 2)
//            .appendLiteral(':')
//            .appendValue(MINUTE_OF_HOUR, 2)
//            .toFormatter())).toAnsi();
//    String input = "";
//    while (!"Yes".equalsIgnoreCase(input.trim())) {
//      try {
//        input = reader.readLine(prompt, rightPrompt, null, "no");
//      } catch (UserInterruptException e) {
//        // Ignore
//      } catch (EndOfFileException e) {
//        return;
//      }
//    }
//
//    terminalLogger().info("Finally!!!! %s", input);


    JRocksPlugin plugin = currentPluginHolder.getCurrentCommand();

    ClassInfoParameter parameter = new BaseClassInfoParameterBuilder()
        .withClassCanonicalName(classCanonicalName)
        .withForce(isForced)
        .withExcludedFields(excludedFields)
        .withIncludedFields(includedFields)
        .withMandatoryFields(mandatoryFields)
        .withSuffix(StringUtils.isNotBlank(suffix) ? suffix : plugin.defaultSuffix())
        .withSuffixToRemove(suffixToRemove)
        .withAdditionalFlags(additionalFlags)
        .build();

    ClassApi classInfo = getClassInfo(parameter);
    terminalLogger().info("Generate %s for %s class with parameters:\n%s", plugin.name(), parameter.classCanonicalName(), parameter);

    final PluginLayout pluginLayout = plugin.layouts().stream()
        .filter(l -> l.name().equalsIgnoreCase(layout))
        .findAny()
        .orElse(plugin.layouts().get(0));

    List<GeneratedSource> generatedSources = pluginLayout.generate(parameter, classInfo);

    System.out.println(generatedSources.get(0).content());

    writerService.writeClass(generatedSources.get(0).content(), parameter, classInfo);
  }

  private Availability generatorAvailability() {
    return configService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you need to execute 'init' command to initialize JRocks!");
  }

  private ClassApi getClassInfo(ClassInfoParameter parameter) {
    io.github.classgraph.ClassInfo sourceClass = classPathScanner.getAllClassInfo()
        .filter(ci -> ci.getName().equals(parameter.classCanonicalName()))
        .findAny()
        .orElseThrow(() -> new IllegalStateException(format("Class '%s' not found into index", parameter.classCanonicalName())));
    return new ClassInfoBuilder(sourceClass).build();
  }

  @Autowired
  @Lazy
  public void setClassPathScanner(ClassPathScanner classPathScanner) {
    this.classPathScanner = classPathScanner;
  }
}
