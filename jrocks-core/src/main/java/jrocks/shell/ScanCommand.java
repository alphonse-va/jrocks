package jrocks.shell;

import jrocks.shell.autocomplete.ClassPathScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ScanCommand {

  @Autowired
  private ClassPathScanner scanner;

  @ShellMethod("Rebuild class path cache autocomplete")
  public void scanClassPath() {
    scanner.rebuid();
  }
}
