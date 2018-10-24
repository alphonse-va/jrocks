package jrocks.shell;

import com.google.common.annotations.VisibleForTesting;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

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
    String formattedMessage = format(message, values);
    Stream.of(formattedMessage.split("\n"))
        .forEach(m -> {
          terminal.writer().print(getMessagePrefix(color).toAnsi());
          terminal.writer().println(new AttributedString(m, AttributedStyle.DEFAULT).toAnsi());
        });
  }

  @Nonnull
  private AttributedString getMessagePrefix(int color) {
    String messagePrefix;
    switch (color) {
      case BLUE:
        messagePrefix = "*";
        break;
      case YELLOW:
        messagePrefix = "*";
        break;
      case RED:
        messagePrefix = "*";
        break;
      case MAGENTA:
        messagePrefix = "*";
        break;
      default:
        messagePrefix = "[I]";
        break;
    }
    return new AttributedString(messagePrefix + " ", AttributedStyle.DEFAULT.foreground(color));
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
