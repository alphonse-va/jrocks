package jrocks.shell.command;

import jrocks.shell.autocomplete.PackageValueProvider;
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
  public ConfigurationCommand(MavenProjectUtil mavenProjectUtil) {
    this.mavenProjectUtil = mavenProjectUtil;
  }

  @ShellMethod(key = "init", value = "Initialize JRocks project")
  public void initialize(@ShellOption(valueProvider = PackageValueProvider.class) String basePackage, boolean force) {
    if (configService().isInitialized() && !force)
      if (!isMavenProject())
        throw new IllegalStateException("init command must be executed from a maven root directory");

    mavenProjectUtil.loadProjects()
        .forEach(mavenProject ->
            configService()
                .addModule(new ModuleConfig()
                    .setName(mavenProject.getName())
                    .setVersion(mavenProject.getVersion())
                    .setSourceDirectory(mavenProject.getBuild().getSourceDirectory())
                    .setOutputDirectory(mavenProject.getBuild().getOutputDirectory())));

    configService().getConfig()
        .setBasePackage(basePackage)
        .setAutoRebuild(true);
    configService().save();
    showConfig();
  }

  @ShellMethod(key = "show-config", value = "Show JRocks configuration")
  void showConfig() {
    terminalLogger().info(configService().getConfig().toString());
    terminalLogger().info(configService().getGlobalConfig().toString());
  }

  @ShellMethod(value = "Show debug information")
  void debug(boolean enable, boolean disable) {
    boolean status = enable || !disable;
    terminalLogger().setVerbose(status);
    String statusString = status ? "enabled" : "disabled";
    terminalLogger().info("Debug information " + statusString);
  }

  @ShellMethodAvailability({"show-config"})
  public Availability availabilityCheck() {
    return configService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }

  private boolean isMavenProject() {
    return new File("pom.xml").exists();
  }
}
