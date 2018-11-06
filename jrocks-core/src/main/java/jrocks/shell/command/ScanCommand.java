package jrocks.shell.command;

import jrocks.shell.ClassPathScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;

@ShellComponent
@ShellCommandGroup("Indexer")
public class ScanCommand extends BaseCommand {

  private final ClassPathScanner scanner;

  @Autowired
  protected ScanCommand(ClassPathScanner scanner) {
    this.scanner = scanner;
  }

  @ShellMethod(key = "rebuild-index", value = "Rebuild index")
  public void scanClassPath() {
    scanner.rebuild();
  }

  @ShellMethod(key = "show-indexed-classes", value = "Show all classes indexed classes")
  public Table showIndexedClasses() {
    TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
    modelBuilder.addRow()
        .addValue("Name")
        .addValue("Path");
    scanner.getAllClassInfo().forEach(c -> {
      modelBuilder
          .addRow()
          .addValue(c.getName())
          .addValue(c.getClasspathElementFile().getAbsolutePath());
    });
    TableBuilder builder = new TableBuilder(modelBuilder.build());
    terminalLogger().newline();
    terminalLogger().info("*Classes*");
    return builder.addFullBorder(BorderStyle.fancy_light).build();
  }

  @ShellMethod(key = "show-index-stats", value = "Show index stats")
  public void show() {
    terminalLogger().info("Number of indexed classes", scanner.getAllClassInfo().count());
  }

  @ShellMethodAvailability({"rebuild-index", "show-indexed-classes", "show-index-stats"})
  public Availability availabilityCheck() {
    return configService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you firstly need to execute 'init' command to initialize your JRocks project!");
  }
}
