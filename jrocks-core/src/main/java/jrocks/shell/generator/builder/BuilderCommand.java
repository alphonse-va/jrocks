package jrocks.shell.generator.builder;

import jrocks.shell.JRocksCommand;
import jrocks.shell.TerminalLogger;
import jrocks.shell.command.GeneratorCommand;
import jrocks.shell.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

@JRocksCommand(value = "Builder Generator", key = BuilderCommand.GENERATOR_NAME, group = "Application")
public class BuilderCommand extends GeneratorCommand {

  static final String GENERATOR_NAME = "builder";

  @Autowired
  public BuilderCommand(BuilderTemplateGenerator templateGenerator, ConfigService configService, TerminalLogger terminalLogger) {
    super(templateGenerator, configService, terminalLogger);
  }

  @Override
  public String name() {
    return GENERATOR_NAME;
  }
}
