package jrocks.shell.command.config;

import jrocks.shell.JRocksConfig;
import jrocks.shell.JRocksProjectConfig;
import jrocks.shell.JRocksProjectConfig.ProjectProperty;
import jrocks.shell.TerminalLogger;
import jrocks.shell.command.BaseCommand;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@ShellComponent
public class ConfigurationCommand extends BaseCommand {

  private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");

  @Autowired
  public ConfigurationCommand(JRocksConfig jRocksConfig, JRocksProjectConfig projectConfig, TerminalLogger terminalLogger) {
    super(jRocksConfig, projectConfig, terminalLogger);
  }

  @ShellMethod(key = "init", value = "Initialize JRocks project", group = "Config")
  public void initialize(String basePackage, boolean force) {
    if (getProjectConfig().isInitialized() && !force)
      if (!isMavenProject())
        throw new IllegalStateException("JRocks command must be executed from a maven root directory");

    MavenProject mavenProject = loadMavenProject();

    JRocksProjectConfig projectConfig = getProjectConfig();
    projectConfig.setSourceDirectory(mavenProject.getBuild().getSourceDirectory());
    projectConfig.setOutputDirectory(mavenProject.getBuild().getOutputDirectory());
    projectConfig.setBuildDirectory(mavenProject.getBuild().getDirectory());
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
  void setConfig(ProjectProperty property, String value) {
    getProjectConfig().storeProperty(property, value);
    getLogger().info("set %s = '%s'", property.getPropertyName(), value);
  }

  @ShellMethod(value = "Show verbose information", group = "Config")
  void verbose() {
    getLogger().setVerbose(true);
    getLogger().info("Verbose information enabled");
  }

  @ShellMethodAvailability({"show-config"})
  public Availability availabilityCheck() {
    return getProjectConfig().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }

  private MavenProject loadMavenProject() {
    File pomFile = new File("pom.xml");
    MavenXpp3Reader mavenReader = new MavenXpp3Reader();
    String effectivePom = callMavenEffectivePom();
    try {
      Model model = mavenReader.read(new StringReader(effectivePom));
      model.setPomFile(pomFile);
      return new MavenProject(model);
    } catch (IOException | XmlPullParserException e) {
      throw new IllegalStateException("Error while reading the effective maven pom", e);
    }
  }

  private String callMavenEffectivePom() {
    ProcessBuilder builder = new ProcessBuilder();
    if (IS_WINDOWS) {
      builder.command("mvn.exe", "help:effective-pom");
    } else {
      builder.command("mvn", "help:effective-pom");
    }
    try {
      Process process = builder.start();
      StringWriter result = new StringWriter();
      StreamConsumer streamConsumer =
          new StreamConsumer(process.getInputStream(), line -> {
            if (!line.startsWith("[") && !line.startsWith("Effective POM")) result.write(line);
          });
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.submit(streamConsumer);
      int exitCode = process.waitFor();
      assert exitCode == 0;
      executorService.shutdown();
      return result.toString();
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException("Error while executing 'mvn help:effective-pom' command", e);
    }
  }

  private boolean isMavenProject() {
    return new File("pom.xml").exists();
  }

  private static class StreamConsumer implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    StreamConsumer(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
  }
}
