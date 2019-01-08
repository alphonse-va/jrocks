package jrocks;

import jrocks.shell.ExtendedDefaultParser;
import jrocks.shell.command.ClassGeneratorCommand;
import jrocks.shell.command.PluginsHolder;
import org.jline.reader.*;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.shell.CompletingParsedLine;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.shell.jline.InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE;
import static org.springframework.shell.jline.ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
public class JRocksShellConfiguration {

  @Autowired
  @Lazy
  private PluginsHolder pluginsHolder;

  @Autowired
  private PromptProvider promptProvider;

  @Autowired
  @Lazy
  private History history;

  @Autowired
  private Shell shell;

  @Bean(destroyMethod = "close")
  public Terminal terminal() {
    try {
      return TerminalBuilder.builder().build();
    } catch (IOException e) {
      throw new BeanCreationException("Could not create Terminal: " + e.getMessage(), e);
    }
  }

  @Bean
  @ConditionalOnProperty(prefix = SPRING_SHELL_INTERACTIVE, value = InteractiveShellApplicationRunner.ENABLED, havingValue = "true", matchIfMissing = true)
  public ApplicationRunner interactiveApplicationRunner(Parser parser, Environment environment) {
    return new InteractiveShellApplicationRunner(lineReader(), promptProvider, parser, shell, environment);
  }

  @Bean
  @ConditionalOnProperty(prefix = SPRING_SHELL_SCRIPT, value = ScriptShellApplicationRunner.ENABLED, havingValue = "true", matchIfMissing = true)
  public ApplicationRunner scriptApplicationRunner(Parser parser, ConfigurableEnvironment environment) {
    return new ScriptShellApplicationRunner(parser, shell, environment);
  }

  @Bean
  public PromptProvider promptProvider() {
    return () -> new AttributedString("jrocks ➜ ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
  }

  /**
   * Installs a default JLine history, and triggers saving to file on context shutdown. Filename is based on
   * {@literal spring.application.name}.
   *
   * @author Eric Bottard
   */
  @Configuration
  @ConditionalOnMissingBean(History.class)
  public static class HistoryConfiguration {

    @Autowired
    @Lazy
    private History history;

    @Bean
    public History history(LineReader lineReader) {
      lineReader.setVariable(LineReader.HISTORY_FILE, Paths.get(".jrocks.log"));
      return new DefaultHistory(lineReader);
    }

    @EventListener
    public void onContextClosedEvent(ContextClosedEvent event) throws IOException {
      history.save();
    }
  }

  @Bean
  public CompleterAdapter completer() {
    return new CompleterAdapter();
  }

  /*
   * Using setter injection to work around a circular dependency.
   */
  @PostConstruct
  public void lateInit() {
    completer().setShell(shell);
    completer().setPluginsHolder(pluginsHolder);
  }

  @Bean
  public Parser parser() {
    ExtendedDefaultParser parser = new ExtendedDefaultParser();
    parser.setEofOnUnclosedQuote(true);
    parser.setEofOnEscapedNewLine(true);
    return parser;
  }

  @Bean
  public LineReader lineReader() {
    LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder()
        .terminal(terminal())
        .appName("JRocks Shell")
        .completer(completer())
        .history(history)
        .highlighter((reader, buffer) -> {
          int l = 0;
          String best = null;
          for (String command : shell.listCommands().keySet()) {
            if (buffer.startsWith(command) && command.length() > l) {
              l = command.length();
              best = command;
            }
          }
          if (best != null) {
            pluginsHolder.initPlugin(best);
            return new AttributedStringBuilder(buffer.length()).append(best, AttributedStyle.BOLD).append(buffer.substring(l)).toAttributedString();
          } else {
            return new AttributedString(buffer, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
          }
        })
        .parser(parser());

    return lineReaderBuilder.build();
  }

  /**
   * A bridge between JLine's {@link Completer} contract and our own.
   *
   * @author Eric Bottard
   */
  public static class CompleterAdapter implements Completer {

    private Shell shell;

    private PluginsHolder pluginsHolder;

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
      CompletingParsedLine cpl = (line instanceof CompletingParsedLine) ? ((CompletingParsedLine) line) : t -> t;

      CompletionContext context = new CompletionContext(sanitizeInput(line.words()), line.wordIndex(), line.wordCursor());

      // TODO find out a commons way to show/hide some parameter based on others
      List<CompletionProposal> proposals = shell.complete(context);
      if (pluginsHolder.getPluginNames().contains(context.getWords().get(0))
          && !context.getWords().contains(ClassGeneratorCommand.PARAM_CLASS)) {
        proposals = proposals.stream()
            .filter(p -> p.value().equals(ClassGeneratorCommand.PARAM_CLASS))
            .collect(Collectors.toList());
      }
      proposals.stream()
          .map(p -> new Candidate(
              p.dontQuote() ? p.value() : cpl.emit(p.value()).toString(),
              p.displayText(),
              p.category(),
              p.description(),
              null,
              null,
              true)
          )
          .forEach(candidates::add);
    }

    void setShell(Shell shell) {
      this.shell = shell;
    }

    void setPluginsHolder(PluginsHolder pluginsHolder) {
      this.pluginsHolder = pluginsHolder;
    }

    /**
     * Sanitize the buffer input given the customizations applied to the JLine parser (<em>e.g.</em> support for
     * line continuations, <em>etc.</em>)
     */
    private static List<String> sanitizeInput(List<String> words) {
      words = words.stream()
          .map(s -> s.replaceAll("^\\n+|\\n+$", "")) // CR at beginning/end of line introduced by backslash continuation
          .map(s -> s.replaceAll("\\n+", " ")) // CR in middle of word introduced by return inside a quoted string
          .collect(Collectors.toList());
      return words;
    }
  }
}
