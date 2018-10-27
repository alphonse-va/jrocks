package jrocks.shell.command;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoBuilder;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.ClassPathScanner;
import jrocks.shell.JRocksCommand;
import jrocks.shell.JRocksShellMethod;
import jrocks.shell.TerminalLogger;
import jrocks.shell.autocomplete.AdditionalFlagValueProvider;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import jrocks.shell.config.ConfigService;
import jrocks.shell.generator.TemplateGenerator;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static java.lang.String.format;

public abstract class ClassGeneratorCommand extends BaseCommand {

  private TemplateGenerator templateGenerator;

  public static final String CLASS_PARAM = "--class";

  @Autowired
  private ClassPathScanner classPathScanner;

  public ClassGeneratorCommand(TemplateGenerator templateGenerator, ConfigService configService, TerminalLogger terminalLogger) {
    super(configService, terminalLogger);
    this.templateGenerator = templateGenerator;
  }

  public abstract String name();

  @ShellMethod
  @JRocksShellMethod
  public void generator(
      @ShellOption(value = CLASS_PARAM, help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = "--suffix-to-remove", help = "Suffix to remove", defaultValue = "") String suffixToRemove,

      @ShellOption(value = "--excluded-fields", help = "Fields to exclude", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = "--included-fields", help = "Fields to include", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = "--mandatory-fields", help = "Mandatory fields", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = "--additional-flags", help = "Generator additional flags", defaultValue = "[]", valueProvider = AdditionalFlagValueProvider.class) String[] additionalFlags,
      @ShellOption(value = "--force", help = "Overwrite existing files") boolean isForced) {

    ClassInfoParameter parameter = new BaseClassInfoParameterBuilder()
        .withClassCanonicalName(classCanonicalName)
        .withForce(isForced)
        .withExcludedFields(excludedFields)
        .withIncludedFields(includedFields)
        .withMandatoryFields(mandatoryFields)
        .withSuffix(templateGenerator.suffix())
        .withSuffixToRemove(suffixToRemove)
        .withAdditionalFlags(additionalFlags)
        .build();

    ClassInfo classInfo = getClassInfo(parameter);
    terminalLogger().info("Generate %s for %s class with parameters:\n%s", this.getClass().getAnnotation(JRocksCommand.class).value(), parameter.classCanonicalName(), parameter);

    templateGenerator.generateSource(parameter, classInfo);
  }

  private Availability generatorAvailability() {
    return configService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("You firstly need to execute 'init' command to initialize your JRocks project!");
  }

  private ClassInfo getClassInfo(ClassInfoParameter parameter) {
    io.github.classgraph.ClassInfo sourceClass = classPathScanner.getAllClassInfo()
        .filter(ci -> ci.getName().equals(parameter.classCanonicalName()))
        .findAny()
        .orElseThrow(() -> new IllegalStateException(format("Class '%s' not found on the class path", parameter.classCanonicalName())));
    return new ClassInfoBuilder(sourceClass).build();
  }
}
