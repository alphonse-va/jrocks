package jrocks.shell.command.bean;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.JRocksConfig;
import jrocks.shell.LogLevel;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import jrocks.shell.command.BaseClassInfoCommand;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.bean.builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static java.util.Arrays.asList;

@ShellComponent
public class BuilderCommand extends BaseClassInfoCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(BuilderCommand.class);

  @Autowired
  public BuilderCommand(JRocksConfig jRocksConfig) {
    super(jRocksConfig);
  }


  @ShellMethod(value = "Builder Generator", key = "builder", group = "builder")
  public void builder(
      @ShellOption(value = "--class", help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = "--suffix", help = "Suffix to add; default Builder", defaultValue = "Builder") String suffix,
      @ShellOption(value = "--suffix-to-remove", help = "Suffix to remove", defaultValue = "") String suffixToRemove,

      @ShellOption(value = "--excluded-fields", help = "Fields to exclude", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = "--included-fields", help = "Fields to include", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = "--mandatory-fields", help = "Mandatory fields", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = "--force", help = "Overwrite existing files") boolean isForced,
      @ShellOption(value = "--log-level", help = "choice the logs level", defaultValue = "info") LogLevel logLevel
  ) {

    final ClassInfoParameterApi parameter = new BaseClassInfoParameterBuilder()
        .setClassCanonicalName(classCanonicalName)
        .setLogLevel(logLevel)
        .setForce(isForced)
        .setExcludedFields(asList(excludedFields))
        .setIncludedFields(asList(includedFields))
        .setMandatoryFields(asList(mandatoryFields))
        .setSuffix(suffix)
        .setSuffixToRemove(suffixToRemove)
        .build();


    setLoggingLevel(logLevel.getLevel());
    getLogger().info("Generate builder for {} class with parameters:\n{}", parameter.getClassCanonicalName(), parameter);

    final ClassInfoApi classInfo = getClassInfoApi(parameter);
    final String generatedSource = builder.template(classInfo, parameter).render().toString();

    writeSource(generatedSource, parameter);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}
