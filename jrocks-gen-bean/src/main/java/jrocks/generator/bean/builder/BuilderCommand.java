package jrocks.generator.bean.builder;

import jrocks.shell.JRocksCommand;
import jrocks.shell.command.ClassGeneratorCommand;
import org.springframework.beans.factory.annotation.Autowired;

@JRocksCommand(value = "Builder Generator", key = BuilderCommand.GENERATOR_NAME, group = "Application")
public class BuilderCommand extends ClassGeneratorCommand {

  static final String GENERATOR_NAME = "builder";

  @Autowired
  public BuilderCommand(BuilderTemplateGenerator templateGenerator) {
    super(templateGenerator);
  }

  @Override
  public String name() {
    return GENERATOR_NAME;
  }
}
