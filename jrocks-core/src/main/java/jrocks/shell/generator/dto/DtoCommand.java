package jrocks.shell.generator.dto;

import jrocks.shell.JRocksCommand;
import jrocks.shell.TerminalLogger;
import jrocks.shell.command.GeneratorCommand;
import jrocks.shell.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@JRocksCommand(value = "DTO generator", key = "dto", group = "Application")
public class DtoCommand extends GeneratorCommand {

  static final String WITH_FACTORY_METHODS_FLAG = "with-factory-method";

  @Autowired
  public DtoCommand(DtoTemplateGenerator templateGenerator, ConfigService configService, TerminalLogger terminalLogger) {
    super(templateGenerator, configService, terminalLogger);
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
