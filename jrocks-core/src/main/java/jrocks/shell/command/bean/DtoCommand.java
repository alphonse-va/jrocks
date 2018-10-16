package jrocks.shell.command.bean;

import jrocks.api.ClassInfoApi;
import jrocks.shell.JRocksConfig;
import jrocks.shell.TerminalLogger;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import jrocks.shell.command.BaseClassInfoCommand;
import jrocks.shell.parameter.BaseClassInfoParameter;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.bean.dto;
import jrocks.template.bean.mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static java.util.Arrays.asList;

@ShellComponent
public class DtoCommand extends BaseClassInfoCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(DtoCommand.class);

  @Autowired
  public DtoCommand(final JRocksConfig jRocksConfig, final TerminalLogger terminalLogger) {
    super(jRocksConfig, terminalLogger);
  }


  @ShellMethod(value = "DTO Generator", key = "dto", group = "bean")
  public void builder(
      @ShellOption(value = "--class", help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = "--suffix", help = "Suffix to add; default DTO", defaultValue = "DTO") String suffix,
      @ShellOption(value = "--suffix-to-remove", help = "Suffix to remove", defaultValue = "") String suffixToRemove,
      @ShellOption(value = "--with-factory-method", help = "Generate DTO with factory method") boolean isWithFactoryMethod,

      @ShellOption(value = "--excluded-fields", help = "Fields to exclude", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = "--included-fields", help = "Fields to include", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = "--mandatory-fields", help = "Mandatory fields", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = "--force", help = "Overwrite existing files") boolean isForced,
      @ShellOption(value = "--verbose", help = "Verbose output") boolean isVerbose
  ) {

    final DtoParameter dtoParameter = new DtoParameterBuilder()
        .setClassInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(classCanonicalName)
            .setForce(isForced)
            .setExcludedFields(asList(excludedFields))
            .setIncludedFields(asList(includedFields))
            .setMandatoryFields(asList(mandatoryFields))
            .setSuffix(suffix)
            .setSuffixToRemove(suffixToRemove)
            .build())
        .setWithFactoryMethod(isWithFactoryMethod)
        .build();

    getLogger().setVerbose(isVerbose);
    getLogger().info("Generate DTO for %s class with parameters:\n%s", dtoParameter.getClassCanonicalName(), dtoParameter);

    final ClassInfoApi classInfo = getClassInfoApi(dtoParameter);
    final String generatedSource = dto.template(classInfo, dtoParameter).render().toString();
    writeSource(generatedSource, dtoParameter);

    if (!isWithFactoryMethod) {
      final BaseClassInfoParameter mapperParameter = new BaseClassInfoParameterBuilder()
          .setClassCanonicalName(classCanonicalName)
          .setForce(isForced)
          .setSuffix(suffix + "Mapper")
          .setSuffixToRemove(suffixToRemove)
          .build();
      String generatedMapperSource = mapper.template(classInfo, dtoParameter).render().toString();
      writeSource(generatedMapperSource, mapperParameter);
    }
  }

}
