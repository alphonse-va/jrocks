package jrocks.shell.config;

import jrocks.shell.ClassPathScanner;
import jrocks.shell.TerminalLogger;
import jrocks.shell.command.BaseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class ScanCommand extends BaseCommand {

  @Autowired
  private ClassPathScanner scanner;

  protected ScanCommand(JRocksConfig jRocksConfig, JRocksProjectConfig projectConfig, TerminalLogger terminalLogger) {
    super(jRocksConfig, projectConfig, terminalLogger);
  }

  @ShellMethod(key = "rebuild-cache", value = "Rebuild cache", group = "Config")
  public void scanClassPath() {
      scanner.rebuild();
  }

  @ShellMethod(key = "show-classes", value = "Show all classes eligible for autocomplete", group = "Config")
  public void show() {
    scanner.getAllClasses().forEach(c -> getLogger().info(c));
  }

  @ShellMethodAvailability({"rebuild-cache", "show-classes"})
  public Availability availabilityCheck() {
    return getProjectConfig().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }
}
