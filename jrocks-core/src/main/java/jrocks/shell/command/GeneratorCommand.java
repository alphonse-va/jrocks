package jrocks.shell.command;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.TerminalLogger;
import jrocks.shell.autocomplete.AdditionalFlagValueProvider;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import jrocks.shell.autocomplete.TemplateGeneratorValueProvider;
import jrocks.shell.config.ConfigService;
import jrocks.shell.generator.TemplateGenerator;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class GeneratorCommand extends BaseClassInfoCommand {

  private final List<TemplateGenerator> templates;

  @Autowired
  public GeneratorCommand(ConfigService configService, TerminalLogger terminalLogger, List<TemplateGenerator> templates) {
    super(configService, terminalLogger);
    this.templates = templates;
  }

  @ShellMethod(value = "Generator", key = "gen", group = "Application")
  public void generator(
      @ShellOption(value = "--generator", help = "Name of the generator", valueProvider = TemplateGeneratorValueProvider.class) String generatorName,
      @ShellOption(value = "--class", help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = "--suffix-to-remove", help = "Suffix to remove", defaultValue = "") String suffixToRemove,

      @ShellOption(value = "--excluded-fields", help = "Fields to exclude", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = "--included-fields", help = "Fields to include", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = "--mandatory-fields", help = "Mandatory fields", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = "--additional-flags", help = "Generator specific additional flags", defaultValue = "[]", valueProvider = AdditionalFlagValueProvider.class) String[] additionalFlags,
      @ShellOption(value = "--force", help = "Overwrite existing files") boolean isForced
  ) {

    TemplateGenerator template = templates.stream()
        .filter(t -> t.getName().equals(generatorName))
        .findAny()
        .orElseThrow(() -> new IllegalStateException(String.format("Generator named '%s' doesn't exist, please review your inputs.", generatorName)));

    ClassInfoParameter parameter = new BaseClassInfoParameterBuilder()
        .setClassCanonicalName(classCanonicalName)
        .setForce(isForced)
        .setExcludedFields(excludedFields)
        .setIncludedFields(includedFields)
        .setMandatoryFields(mandatoryFields)
        .setSuffix(template.getSuffix())
        .setSuffixToRemove(suffixToRemove)
        .setAddtionalFlags(additionalFlags)
        .build();

    ClassInfo classInfo = getClassInfoApi(parameter);
    getLogger().info("Generate %s for %s class with parameters:\n%s", template.getName(), parameter.getClassCanonicalName(), parameter);

    template.generateSource(parameter, classInfo);
  }

  @ShellMethodAvailability("generator")
  private Availability availabilityCheck() {
    return getConfigService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }
}
