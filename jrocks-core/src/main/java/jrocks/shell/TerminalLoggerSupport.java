package jrocks.shell;

import com.google.common.annotations.VisibleForTesting;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.jline.utils.AttributedStyle.*;

/**
 * Supported syntax
 *
 * <code>
 * Character styles<br>
 * *Italic characters*<br>
 * **bold characters**<br>
 * __bold characters__<br>
 * ~~strikethrough text~~<br>
 * Unordered list<br>
 * *  Item 1<br>
 * *  Item 2<br>
 * *    Item 3<br>
 * *    Item 3a<br>
 * *    Item 3b<br>
 * *    Item 3c<br>
 * </code>
 */
@Service
public class TerminalLoggerSupport implements TerminalLogger {

  @Autowired
  @Lazy
  private Terminal terminal;

  private boolean verbose;

  enum LogLevel {
    INFO,
    WARNING,
    ERROR,
    VERBOSE
  }

  @Override
  public void info(String message, Object... values) {
    printMessage(message, values, LogLevel.INFO);
  }

  @Override
  public void warning(String message, Object... values) {
    printMessage(message, values, LogLevel.WARNING);
  }

  @Override
  public void error(String message, Object... values) {
    printMessage(message, values, LogLevel.ERROR);
  }

  @Override
  public void verbose(String message, Object... values) {
    if (verbose) printMessage(message, values, LogLevel.VERBOSE);
  }

  private void printMessage(String message, Object[] values, LogLevel level) {

    terminal.writer().print(new AttributedString(getMessagePrefix(), AttributedStyle.DEFAULT).toAnsi());

    String formattedMessage = format(message, values);
    Matcher matcher = Pattern.compile("([_|\\\\*]+?(.+?)[_|\\\\*])+?").matcher(formattedMessage);

    int lastMatchIdx = 0;
    while (matcher.find()) {
      final String withDelimiters = matcher.group(1);

      final int idxOfMatcher = formattedMessage.substring(lastMatchIdx).indexOf(withDelimiters) + lastMatchIdx;

      final String unformattedText = formattedMessage.substring(lastMatchIdx, idxOfMatcher);
      lastMatchIdx = lastMatchIdx + unformattedText.length() + withDelimiters.length();
      if (withDelimiters.startsWith("*")) {
        terminal.writer().print(
            new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.faint().foreground(colorFromLevel(level)))
                .append(unformattedText).toAnsi());
        terminal.writer().print(
            new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.bold().foreground(colorFromLevel(level)))
                .append(matcher.group(2)).toAnsi());
      } else if (withDelimiters.startsWith("_")) {
        terminal.writer().print(
            new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.faint().foreground(colorFromLevel(level)))
                .append(unformattedText).toAnsi());

        terminal.writer().print(
            new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.italic().foreground(colorFromLevel(level)))
                .append(matcher.group(2)).toAnsi());
      }
    }
    terminal.writer().println(
        new AttributedStringBuilder()
            .style(AttributedStyle.DEFAULT.faint().foreground(colorFromLevel(level)))
            .append(formattedMessage.substring(lastMatchIdx)).toAnsi());
  }


  private int colorFromLevel(LogLevel level) {
    int color;
    switch (level) {
      case VERBOSE:
        color = MAGENTA;
        break;
      case WARNING:
        color = YELLOW;
        break;
      case ERROR:
        color = RED;
        break;
      case INFO:
      default:
        color = WHITE;
        break;
    }
    return color;
  }

  @Nonnull
  private String getMessagePrefix() {
    return " ";
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
