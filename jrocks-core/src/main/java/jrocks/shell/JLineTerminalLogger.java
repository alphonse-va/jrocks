package jrocks.shell;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalLogger {

  private final Terminal terminal;

  @Autowired
  public TerminalLogger(final Terminal terminal) {this.terminal = terminal;}

  public void info(String message) {
    terminal.writer().println(new AttributedString(message,
        AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)).toAnsi());
  }

  public void warning(String message) {
    terminal.writer().println(new AttributedString(message,
        AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi());
  }

  public void error(String message) {
    terminal.writer().println(new AttributedString(message,
        AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).toAnsi());
  }

}
