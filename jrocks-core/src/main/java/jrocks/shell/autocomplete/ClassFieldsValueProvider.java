package jrocks.shell.autocomplete;

import com.google.common.annotations.VisibleForTesting;
import jrocks.shell.ClassPathScanner;
import jrocks.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

@Component
public class ClassFieldsValueProvider extends ValueProviderSupport {

  private final ClassPathScanner classPathScanner;

  private final TerminalLogger logger;

  @Autowired
  public ClassFieldsValueProvider(ClassPathScanner classPathScanner, TerminalLogger logger) {
    this.classPathScanner = classPathScanner;
    this.logger = logger;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return getSourceClassName(completionContext)
        .map(cp -> getCompletionProposals(cp, completionContext))
        .orElse(new ArrayList<>());
  }

  @VisibleForTesting
  List<CompletionProposal> getCompletionProposals(String className, CompletionContext completionContext) {
    String word = completionContext.currentWordUpToCursor();
    String lastProposal = substringAfterLast(word, ",").isEmpty()
        ? word.split(",")[word.split(",").length - 1]
        : substringAfterLast(word, ",");


    boolean lastProposalIsField = classPathScanner.getAllFieldsWithGetterAndSetters(className)
        .stream().anyMatch(name -> !lastProposal.isEmpty() && name.startsWith(lastProposal.trim()));

    return classPathScanner.getAllFieldsWithGetterAndSetters(className).stream()
        .filter(name -> isFieldAlreadyContained(completionContext, name))
        .map(fieldName -> lastProposalIsField
            ? new CompletionProposal(format("%s,%s", word, fieldName).replace(",,", ",").trim())
            : new CompletionProposal(fieldName))
        .collect(Collectors.toList());
  }

  private boolean isFieldAlreadyContained(CompletionContext completionContext, CharSequence name) {
    return Arrays.stream(completionContext.currentWord().split(","))
        .noneMatch(w -> w.contains(name));
  }

  @VisibleForTesting
  Optional<String> getSourceClassName(CompletionContext completionContext) {
    List<String> words = completionContext.getWords();
    OptionalInt classIdx = IntStream.range(0, words.size() - 1)
        .filter(i -> words.get(i).equals("--class"))
        .findAny();

    if (!classIdx.isPresent()) {
      logger.verbose("--class parameter must be specified");
      return Optional.empty();
    }
    String className = words.get(classIdx.getAsInt() + 1);
    return Optional.ofNullable(className);
  }
}