package jrocks.shell.command.bean;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.JRocksConfig;
import jrocks.shell.TerminalLogger;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import jrocks.shell.command.BaseClassInfoCommand;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.bean.builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static java.util.Arrays.asList;

@ShellComponent
public class BuilderCommand extends BaseClassInfoCommand {

  @Autowired
  public BuilderCommand(final JRocksConfig jRocksConfig, final TerminalLogger terminalLogger) {
    super(jRocksConfig, terminalLogger);
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
      @ShellOption(value = "--verbose", help = "Verbose output") boolean isVerbose
  ) {

    final ClassInfoParameterApi parameter = new BaseClassInfoParameterBuilder()
        .setClassCanonicalName(classCanonicalName)
        .setForce(isForced)
        .setExcludedFields(asList(excludedFields))
        .setIncludedFields(asList(includedFields))
        .setMandatoryFields(asList(mandatoryFields))
        .setSuffix(suffix)
        .setSuffixToRemove(suffixToRemove)
        .build();

    getLogger().setVerbose(isVerbose);

    final ClassInfoApi classInfo = getClassInfoApi(parameter);
    final String generatedSource = builder.template(classInfo, parameter).render().toString();

    writeSource(generatedSource, parameter);
  }
}
