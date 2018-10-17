package jrocks.shell;

import jrocks.ClassPathScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
public class ScanCommand {

  @Autowired
  private ClassPathScanner scanner;

  @ShellMethod("Rebuild class path cache autocomplete")
  public void scanClassPath() {
    scanner.rebuid();
    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
  }
}
