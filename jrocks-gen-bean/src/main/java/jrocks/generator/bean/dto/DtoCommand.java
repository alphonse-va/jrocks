package jrocks.generator.bean.dto;

import jrocks.shell.JRocksCommand;
import jrocks.shell.command.ClassGeneratorCommand;

import java.util.Collections;
import java.util.List;

@JRocksCommand(value = "DTO generator", key = "dto", group = "Application")
public class DtoCommand extends ClassGeneratorCommand {

  static final String WITH_FACTORY_METHODS_FLAG = "with-factory-method";

  public DtoCommand(DtoTemplateGenerator templateGenerator) {
    super(templateGenerator);
  }

  @Override
  public List<String> additionalFlags() {
    return Collections.singletonList(WITH_FACTORY_METHODS_FLAG);
  }

  @Override
  public String name() {
    return "dto";
  }
}
