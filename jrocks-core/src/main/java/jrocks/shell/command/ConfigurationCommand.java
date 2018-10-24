package jrocks.shell.command;

import jrocks.shell.TerminalLogger;
import jrocks.shell.autocomplete.PackageValueProvider;
import jrocks.shell.config.ConfigService;
import jrocks.shell.config.MavenProjectUtil;
import jrocks.shell.config.ModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import java.io.File;

@ShellComponent
@ShellCommandGroup(value = "Configuration")
public class ConfigurationCommand extends BaseCommand {

  private final MavenProjectUtil mavenProjectUtil;

  @Autowired
  public ConfigurationCommand(ConfigService configService, TerminalLogger terminalLogger, MavenProjectUtil mavenProjectUtil) {
    super(configService, terminalLogger);
    this.mavenProjectUtil = mavenProjectUtil;
  }

  @ShellMethod(key = "init", value = "Initialize JRocks project")
  public void initialize(@ShellOption(valueProvider = PackageValueProvider.class) String basePackage, boolean force) {
    if (getConfigService().isInitialized() && !force)
      if (!isMavenProject())
        throw new IllegalStateException("init command must be executed from a maven root directory");

    mavenProjectUtil.loadProjects()
        .forEach(mavenProject ->
            getConfigService()
                .addModule(new ModuleConfig()
                    .setName(mavenProject.getName())
                    .setVersion(mavenProject.getVersion())
                    .setSourceDirectory(mavenProject.getBuild().getSourceDirectory())
                    .setOutputDirectory(mavenProject.getBuild().getOutputDirectory())));

    getConfigService().getConfig()
        .setBasePackage(basePackage)
        .setAutoRebuild(true);
    getConfigService().save();
    showConfig();
  }

  @ShellMethod(key = "show-config", value = "Show JRocks configuration")
  void showConfig() {
    getLogger().info(getConfigService().getConfig().toString());
    getLogger().info(getConfigService().getGlobalConfig().toString());
  }

  @ShellMethod(value = "Show debug information")
  void debug(boolean enable, boolean disable) {
    boolean status = enable || !disable;
    getLogger().setVerbose(status);
    String statusString = status ? "enabled" : "disabled";
    getLogger().info("Debug information " + statusString);
  }

  @ShellMethodAvailability({"show-config"})
  public Availability availabilityCheck() {
    return getConfigService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }

  private boolean isMavenProject() {
    return new File("pom.xml").exists();
  }
}
