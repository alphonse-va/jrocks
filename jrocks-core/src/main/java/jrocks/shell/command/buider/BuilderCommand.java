package jrocks.shell.command.buider;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.JRocksConfig;
import jrocks.shell.command.BaseClassInfoCommand;
import jrocks.template.bean.builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
public class BuilderCommand extends BaseClassInfoCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(BuilderCommand.class);

  @Autowired
  public BuilderCommand(JRocksConfig jRocksConfig) {
    super(jRocksConfig);
  }

  @Override
  protected String generateSource(final ClassInfoParameterApi parameter, final ClassInfoApi classInfo) {
    return builder.template(classInfo, parameter).render().toString();
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}
