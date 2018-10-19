package jrocks.shell.command;

import jrocks.shell.ClassPathScanner;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.JRocksConfig;
import jrocks.shell.config.JRocksProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@ShellCommandGroup("Indexer")
public class ScanCommand extends BaseCommand {

  private final ClassPathScanner scanner;

  @Autowired
  protected ScanCommand(JRocksConfig jRocksConfig, JRocksProjectConfig projectConfig, TerminalLogger terminalLogger, final ClassPathScanner scanner) {
    super(jRocksConfig, projectConfig, terminalLogger);
    this.scanner = scanner;
  }

  @ShellMethod(key = "rebuild-index", value = "Rebuild index")
  public void scanClassPath() {
      scanner.rebuild();
  }

  @ShellMethod(key = "show-indexed-classes", value = "Show all classes indexed classes")
  public void showIndexedClasses() {
    scanner.getAllClasses().forEach(c -> getLogger().info(c));
  }

  @ShellMethod(key = "show-index-stats", value = "Show index stats")
  public void show() {
    scanner.getAllClassInfo().count();
  }

  @ShellMethodAvailability({"rebuild-cache", "show-classes"})
  public Availability availabilityCheck() {
    return getProjectConfig().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }
}
