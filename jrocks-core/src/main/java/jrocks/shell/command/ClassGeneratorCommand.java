package jrocks.shell.command;

import jrocks.model.ClassInfoBuilder;
import jrocks.plugin.api.*;
import jrocks.shell.BaseTerminalLogger;
import jrocks.shell.ClassPathScanner;
import jrocks.shell.JRocksShellMethod;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.shell.valueprovider.AdditionalFlagValueProvider;
import jrocks.shell.valueprovider.AllClassValueProvider;
import jrocks.shell.valueprovider.ClassFieldsValueProvider;
import jrocks.shell.valueprovider.GeneratorValueProvider;
import jrocks.shell.writer.ResultWriterService;
import org.jline.reader.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class ClassGeneratorCommand extends BaseCommand {

  public static final String PARAM_CLASS = "--class";
  private static final String PARAM_SUFFIX = "--suffix";
  private static final String PARAM_SUFFIX_TO_REMOVE = "--suffix-to-remove";
  private static final String PARAM_EXCLUDED_FIELDS = "--excluded-fields";
  private static final String PARAM_INCLUDED_FIELDS = "--included-fields";
  private static final String PARAM_MANDATORY_FIELDS = "--mandatory-fields";
  private static final String PARAM_ADDITIONAL_FLAGS = "--additional-flags";
  private static final String PARAM_FORCE = "--force";
  private static final String PARAM_GENERATOR = "--generator";
  private static final String PARAM_DRY = "--dry-run";

  private ClassPathScanner classPathScanner;

  @Autowired
  private ResultWriterService writerService;

  @Autowired
  private PluginsHolder pluginsHolder;

  @ShellMethod
  @JRocksShellMethod
  public void generator(
      @ShellOption(value = PARAM_CLASS, help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = PARAM_SUFFIX, help = "Suffix to remove", defaultValue = "") String suffix,
      @ShellOption(value = PARAM_SUFFIX_TO_REMOVE, help = "Suffix to remove", defaultValue = "") String suffixToRemove,
      @ShellOption(value = PARAM_EXCLUDED_FIELDS, help = "Fields to exclude", defaultValue = "", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = PARAM_INCLUDED_FIELDS, help = "Fields to include", defaultValue = "", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = PARAM_MANDATORY_FIELDS, help = "Mandatory fields", defaultValue = "", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = PARAM_ADDITIONAL_FLAGS, help = "Generator additional flags", defaultValue = "", valueProvider = AdditionalFlagValueProvider.class) String[] additionalFlags,
      @ShellOption(value = PARAM_GENERATOR, help = "Template generator", defaultValue = "", valueProvider = GeneratorValueProvider.class) String generator,
      @ShellOption(value = PARAM_FORCE, help = "Overwrite existing files") boolean isForced,
      @ShellOption(value = PARAM_DRY, help = "Only print result") boolean dryRun) {

    JRocksPlugin plugin = pluginsHolder.getCurrentPlugin();

    Optional<PluginGenerator> pluginGeneratorOptional = plugin.generators().stream()
        .filter(Objects::nonNull)
        .filter(l -> Objects.equals(l.name(), generator))
        .findAny();

    PluginGenerator pluginGenerator;
    if (pluginGeneratorOptional.isPresent()) {
      pluginGenerator = pluginGeneratorOptional.get();
    } else {
      if (isEmpty(plugin.generators())) {
        throw new JRocksShellCommandException("No generator found for plugin: " + plugin);
      }
      pluginGenerator = plugin.generators().get(0);
      terminalLogger().info(plugin, "no generator specified, using _%s_", pluginGenerator.name());
    }

    ClassParameterApi parameter = new BaseClassInfoParameterBuilder()
        .withClassCanonicalName(classCanonicalName)
        .withForce(isForced)
        .withExcludedFields(excludedFields)
        .withIncludedFields(includedFields)
        .withMandatoryFields(mandatoryFields)
        .withSuffix(isNotBlank(suffix) ? suffix : plugin.defaultSuffix())
        .withSuffixToRemove(suffixToRemove)
        .withAdditionalFlags(additionalFlags)
        .withLayout(pluginGenerator)
        .withDriRun(dryRun)
        .build();
    ClassApi classInfo = getClassInfo(parameter);

    Map<Object, QuestionResponse> responses = askAdditionalQuestions(plugin, parameter, classInfo, isForced);
    parameter.addResponses(responses);
    terminalLogger().verbose(plugin, "*responses:*");
    parameter.responses().forEach((key, response) -> terminalLogger().verbose("%s: _%s_", response.question().text(), response.text()));

    terminalLogger().info(plugin, "receive followed parameters:\n\t" +
            "generator: _%s_\n\t" +
            "source: _%s_" +
            "%s",
        pluginGenerator.name(), classInfo.name(), parameter);

    pluginGenerator.generate(parameter, classInfo).forEach(source -> {
      if (source.isJava()) writerService.writeClass(source, parameter, classInfo);
      else throw new IllegalStateException("Not yet implemented");
    });
  }

  private Map<Object, QuestionResponse> askAdditionalQuestions(JRocksPlugin plugin, ClassParameterApi parameter, ClassApi classInfo, boolean force) {
    Map<Object, QuestionResponse> result = new HashMap<>();
    Map<Object, Question> questions = plugin.additionalQuestions(parameter, classInfo);
    for (Map.Entry<Object, Question> entry : questions.entrySet()) {
      Object key = entry.getKey();
      Question question = entry.getValue();
      if (question.hasDefaultValue() && force) {
        result.put(key, new QuestionResponseSupport().setQuestion(question).setResponse(question.buffer()));
        terminalLogger().info(plugin, "use default parameter value *%s* for '_%s_' question.", question.buffer(), question.text());
        continue;
      }

      List<String> proposals = question.proposals() != null ? question.proposals() : new ArrayList<>();
      LineReader reader = LineReaderBuilder
          .builder()
          .highlighter((reader1, buffer) -> new AttributedString(buffer, AttributedStyle.BOLD.foreground(AttributedStyle.WHITE)))
          .completer((reader1, line, candidates) -> proposals.forEach(p -> candidates.add(new Candidate(p)))).build();
      reader.unsetOpt(LineReader.Option.INSERT_TAB);
      AttributedStringBuilder promptBuilder = new AttributedStringBuilder()
          .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
          .append(termFormat(question.text()))
          .style(AttributedStyle.DEFAULT);
      if (!proposals.isEmpty())
        promptBuilder
            .append(" [")
            .append(String.join("|", proposals))
            .append("]:");

      String rightPrompt = new AttributedStringBuilder()
          .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
          .append(" ")
          .append(plugin.name())
          .append("(v")
          .append(plugin.version())
          .append(") ")
          .toAnsi();
      String input = question.buffer();
      while (proposals.isEmpty() || !proposals.contains(input.trim())) {
        try {
          input = reader.readLine(promptBuilder.append("➜ ").toAnsi(), rightPrompt, question.mask(), question.buffer());
          proposals.add(input);
        } catch (UserInterruptException | EndOfFileException e) {
          // ignored
          terminalLogger().verbose(plugin, e.getMessage());
        }
      }
      result.put(key, new QuestionResponseSupport().setQuestion(question).setResponse(input));
    }
    return result;
  }

  /**
   * TODO merge with {@link jrocks.shell.BaseTerminalLogger#printMessage(String, Object[], BaseTerminalLogger.LogLevel)}
   */
  private static String termFormat(String message) {

    Matcher matcher = Pattern.compile("([_|\\\\*]+?(.+?)[_|\\\\*])+?").matcher(message);

    StringBuilder result = new StringBuilder();

    int lastMatchIdx = 0;
    while (matcher.find()) {
      String withDelimiters = matcher.group(1);
      int idxOfMatcher = message.substring(lastMatchIdx).indexOf(withDelimiters) + lastMatchIdx;
      String unformattedText = message.substring(lastMatchIdx, idxOfMatcher);
      lastMatchIdx = lastMatchIdx + unformattedText.length() + withDelimiters.length();

      result.append(new AttributedStringBuilder()
          .style(AttributedStyle.DEFAULT.faint().foreground(AttributedStyle.WHITE))
          .append(unformattedText).toAnsi());

      if (withDelimiters.startsWith("*")) {
        result.append(new AttributedStringBuilder()
            .style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.WHITE))
            .append(matcher.group(2)).toAnsi());
      } else if (withDelimiters.startsWith("_")) {
        result.append(new AttributedStringBuilder()
            .style(AttributedStyle.DEFAULT.italic().foreground(AttributedStyle.WHITE))
            .append(matcher.group(2)).toAnsi());
      }
    }
    result.append(
        new AttributedStringBuilder()
            .style(AttributedStyle.DEFAULT.faint().foreground(AttributedStyle.WHITE))
            .append(message.substring(lastMatchIdx)).toAnsi());
    return result.toString();
  }

  private Availability generatorAvailability() {
    return configService().isInitialized()
        ? Availability.available()
        : Availability.unavailable("you need to execute 'init' command to initialize JRocks");
  }

  private ClassApi getClassInfo(ClassParameterApi parameter) {
    io.github.classgraph.ClassInfo sourceClass = classPathScanner.getAllClassInfo()
        .filter(ci -> ci.getName().equals(parameter.classCanonicalName()))
        .findAny()
        .orElseThrow(() -> new JRocksShellCommandException(String.format("Class '%s' not found into index", parameter.classCanonicalName())));
    return new ClassInfoBuilder(sourceClass).build();
  }

  @Autowired
  @Lazy
  public void setClassPathScanner(ClassPathScanner classPathScanner) {
    this.classPathScanner = classPathScanner;
  }
}
