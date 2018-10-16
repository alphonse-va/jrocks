package jrocks.shell;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class JLineTerminalLogger implements TerminalLogger {

  @Autowired
  private Terminal terminal;

  private boolean verbose;

  @Override
  public void info(String message, Object... values) {
    printMessage(message, values, AttributedStyle.BLUE);
  }

  @Override
  public void warning(String message, Object... values) {
    printMessage(message, values, AttributedStyle.YELLOW);
  }

  @Override
  public void error(String message, Object... values) {
    printMessage(message, values, AttributedStyle.RED);
  }

  @Override
  public void verbose(final String message, final Object... values) {
    if (verbose) printMessage(message, values, AttributedStyle.MAGENTA);
  }

  private void printMessage(final String message, final Object[] values, final int red) {
    terminal.writer().println(new AttributedString(format(message, values),
        AttributedStyle.DEFAULT.foreground(red)).toAnsi());
  }

  @Override
  public void setVerbose(final boolean verbose) {
    this.verbose = verbose;
  }
}
