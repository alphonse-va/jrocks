package jrocks.shell.command;

import jrocks.shell.ClassPathScanner;
import jrocks.shell.valueprovider.PackageValueProvider;
import jrocks.shell.config.MavenService;
import jrocks.shell.config.ModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;

import java.io.File;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup(value = "Configuration")
public class ConfigurationCommand extends BaseCommand {

  private final MavenService mavenService;

  @Autowired
  private ClassPathScanner classPathScanner;

  @Autowired
  private PluginsHolder pluginsHolder;

  @Autowired
  public ConfigurationCommand(MavenService mavenService) {
    this.mavenService = mavenService;
  }

  @ShellMethod(key = "init", value = "Initialize JRocks project")
  public void initialize(@ShellOption(valueProvider = PackageValueProvider.class) String basePackage, boolean force) {
    if (configService().isInitialized() && !force)
      if (!isMavenProject())
        throw new IllegalStateException("init command must be executed from a maven root directory");

    mavenService.loadProjects()
        .forEach(mavenProject -> configService()
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
    terminalLogger().info(configService().globalConfig().toString());
  }

  @ShellMethod(value = "Show debug information")
  void debug(boolean enable, boolean disable) {
    boolean status = enable || !disable;
    terminalLogger().setVerbose(status);
    String statusString = status ? "enabled" : "disabled";
    terminalLogger().info("Debug information " + statusString);
  }

  @ShellMethod("Show plugins")
  Table showPlugins() {
    TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
    modelBuilder.addRow()
        .addValue("Name")
        .addValue("Description")
        .addValue("Keys")
        .addValue("Group")
        .addValue("Generator");
    pluginsHolder.getPlugins()
        .forEach(p -> {
          String generator = p.generators().stream()
              .map(l -> String.format("%s (%s)", l.description(), l.version()))
              .collect(Collectors.joining("\n"));
          modelBuilder.addRow()
              .addValue(p.name())
              .addValue(p.description())
              .addValue(p.keys())
              .addValue(p.group())
              .addValue(generator);
        });
    TableBuilder builder = new TableBuilder(modelBuilder.build());
    terminalLogger().newline();
    terminalLogger().info("*Plugins*");
    return builder.addFullBorder(BorderStyle.fancy_light).build();
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

  public void setClassPathScanner(ClassPathScanner classPathScanner) {
    this.classPathScanner = classPathScanner;
  }
}
