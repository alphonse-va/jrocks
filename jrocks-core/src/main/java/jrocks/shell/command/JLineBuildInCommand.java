package jrocks.shell.command;

import jrocks.plugin.api.shell.TerminalLogger;
import org.jline.builtins.Less;
import org.jline.builtins.Nano;
import org.jline.builtins.Source;
import org.jline.builtins.TTop;
import org.jline.reader.History;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;

@ShellComponent
@ShellCommandGroup("Tools")
public class JLineBuildInCommand extends BaseCommand {

  private Terminal terminal;

  private final History history;

  private final TerminalLogger terminalLogger;

  private Less less;

  @Autowired
  public JLineBuildInCommand(History history, TerminalLogger terminalLogger) {
    this.history = history;
    this.terminalLogger = terminalLogger;
  }

  @PostConstruct
  private void initLess() {
    less = new Less(terminal);
  }

  @ShellMethod("Less unix like command.")
  public void less(@ShellOption(help = "Target file") File file) {
    try {
      less.run(new Source.PathSource(file, file.getName()));
    } catch (IOException | InterruptedException e) {
      throw new JRocksShellCommandException(e);
    }
  }

  @ShellMethod("Nano's ANOther editor, an enhanced free Pico clone")
  public void nano(@ShellOption(help = "File to edit") File file) {
    try {
      Nano nano = new Nano(terminal, file.toPath());
      nano.printLineNumbers = true;
      nano.mouseSupport = true;
      nano.open(file.getAbsolutePath());
      nano.run();
    } catch (IOException e) {
      throw new JRocksShellCommandException(e);
    }
  }

  @ShellMethod("Display threads activity")
  public void top() {
    try {
      TTop top = new TTop(terminal);
      top.run();
    } catch (IOException | InterruptedException e) {
      throw new JRocksShellCommandException(e);
    }
  }

  @ShellMethod("Display command history")
  public void history() {
    StreamSupport.stream(history.spliterator(), false)
        .forEach(h -> terminalLogger.info("%s: %s", h.index(), h.line()));
  }

  @Autowired
  @Lazy
  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }
}
