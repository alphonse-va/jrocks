package jrocks.shell.config;

import jrocks.shell.TerminalLogger;
import jrocks.shell.autocomplete.PackageValueProvider;
import jrocks.shell.command.BaseCommand;
import jrocks.shell.config.JRocksProjectConfig.PropertyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.io.File;

@ShellComponent
public class ConfigurationCommand extends BaseCommand {

  private final MavenProjectUtil mavenProjectUtil;

  @Autowired
  public ConfigurationCommand(JRocksConfig jRocksConfig, JRocksProjectConfig projectConfig, TerminalLogger terminalLogger, MavenProjectUtil mavenProjectUtil) {
    super(jRocksConfig, projectConfig, terminalLogger);
    this.mavenProjectUtil = mavenProjectUtil;
  }

  @ShellMethod(key = "init", value = "Initialize JRocks project", group = "Config")
  public void initialize(@ShellOption(valueProvider = PackageValueProvider.class) String basePackage, boolean force) {
    if (getProjectConfig().isInitialized() && !force)
      if (!isMavenProject())
        throw new IllegalStateException("init command must be executed from a maven root directory");

    JRocksProjectConfig projectConfig = getProjectConfig();
    projectConfig.clearDirectories();
    mavenProjectUtil.loadProjects()
        .forEach(mavenProject -> {
          projectConfig.addSourceDirectory(mavenProject.getBuild().getSourceDirectory());
          projectConfig.addOutputDirectory(mavenProject.getBuild().getOutputDirectory());
          projectConfig.addBuildDirectory(mavenProject.getBuild().getDirectory());
        });
    projectConfig.setBasePackage(basePackage);
    projectConfig.setAutoRebuild(true);
    projectConfig.store();
    showConfig();
  }

  @ShellMethod(key = "show-config", value = "Show JRocks configuration", group = "Config")
  void showConfig() {
    getLogger().info("\n" + getProjectConfig().toString() + "\n");
    getLogger().info(getConfig().toString() + "\n");
  }

  @ShellMethod(key = "set-config", value = "Set Configuration Property", group = "Config")
  void setConfig(PropertyCode property, String value) {
    getProjectConfig().storeProperty(property, value);
    getLogger().info("✔ %s='%s'", property.getPropertyName(), value);
  }

  @ShellMethod(value = "Show debug information", group = "Config")
  void debug(boolean enable, boolean disable) {
    boolean status = enable || !disable;
    getLogger().setVerbose(status);
    String statusString = status ? "enabled" : "disabled";
    getLogger().info("Debug information " + statusString);
  }

  @ShellMethodAvailability({"show-config"})
  public Availability availabilityCheck() {
    return getProjectConfig().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }

  private boolean isMavenProject() {
    return new File("pom.xml").exists();
  }
}
