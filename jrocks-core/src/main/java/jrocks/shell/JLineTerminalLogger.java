package jrocks.shell;

import com.google.common.annotations.VisibleForTesting;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static java.lang.String.format;
import static org.jline.utils.AttributedStyle.*;

@Service
public class JLineTerminalLogger implements TerminalLogger {

  @Autowired
  private Terminal terminal;

  private boolean verbose;

  @Override
  public void info(String message, Object... values) {
    printMessage(message, values, BLUE);
  }

  @Override
  public void warning(String message, Object... values) {
    printMessage(message, values, YELLOW);
  }

  @Override
  public void error(String message, Object... values) {
    printMessage(message, values, RED);
  }

  @Override
  public void verbose(String message, Object... values) {
    if (verbose) printMessage(message, values, MAGENTA);
  }

  private void printMessage(String message, Object[] values, int color) {

    AttributedString messagePrefix;
    switch (color) {
      case BLUE:
        messagePrefix = getMessagePrefix(color, "[INFO] ");
        break;
      case YELLOW:
        messagePrefix = getMessagePrefix(color, "[WARN] ");
        break;
      case RED:
        messagePrefix = getMessagePrefix(color, "[ERROR] ");
        break;
      case MAGENTA:
        messagePrefix = getMessagePrefix(color, "[VERB] ");
        break;
      default:
        messagePrefix = getMessagePrefix(color, "[INFO] ");
        break;
    }

    terminal.writer().print(messagePrefix.toAnsi());
    terminal.writer().println(new AttributedString(format(message, values), AttributedStyle.DEFAULT).toAnsi());
  }

  @Nonnull
  private AttributedString getMessagePrefix(int color, String s) {
    return new AttributedString(s, AttributedStyle.DEFAULT.foreground(color));
  }

  @Override
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  @VisibleForTesting
  void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }
}
