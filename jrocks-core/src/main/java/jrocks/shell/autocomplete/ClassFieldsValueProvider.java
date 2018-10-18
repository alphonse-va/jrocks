package jrocks.shell.autocomplete;

import com.google.common.annotations.VisibleForTesting;
import jrocks.ClassPathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static org.apache.commons.lang3.StringUtils.*;

@Component
public class ClassFieldsValueProvider extends ValueProviderSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassFieldsValueProvider.class);

  private final ClassPathScanner classPathScanner;

  @Autowired
  public ClassFieldsValueProvider(ClassPathScanner classPathScanner) {this.classPathScanner = classPathScanner;}

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return getSourceClass(completionContext)
        .map(cp -> getCompletionProposals(cp, completionContext))
        .orElse(new ArrayList<>());
  }

  @VisibleForTesting
  List<CompletionProposal> getCompletionProposals(Class<?> clazz, CompletionContext completionContext) {
    String word = completionContext.currentWordUpToCursor();
    String lastProposal = substringAfterLast(word, ",").isEmpty()
        ? word.split(",")[word.split(",").length - 1]
        : substringAfterLast(word, ",");


    boolean lastProposalIsField = classPathScanner.getAllFieldsWithGetterAndSetters(clazz.getCanonicalName())
        .stream().anyMatch(name -> !lastProposal.isEmpty() && name.startsWith(lastProposal.trim()));

    return classPathScanner.getAllFieldsWithGetterAndSetters(clazz.getCanonicalName()).stream()
        .filter(name -> isFieldAlreadyContained(completionContext, name))
        .map(fieldName -> lastProposalIsField
            ? new CompletionProposal(format("%s,%s", word, fieldName).replace(",,", ",").trim())
            : new CompletionProposal(fieldName))
        .collect(Collectors.toList());
  }

  private boolean isFieldAlreadyContained(CompletionContext completionContext, String name) {
    return Arrays.stream(completionContext.currentWord().split(","))
        .noneMatch(w -> w.contains(name));
  }

  @VisibleForTesting
  Optional<Class<?>> getSourceClass(CompletionContext completionContext) throws IllegalStateException {
    List<String> words = completionContext.getWords();
    OptionalInt classIdx = IntStream.range(0, words.size() - 1)
        .filter(i -> words.get(i).equals("--class"))
        .findAny();

    if (!classIdx.isPresent()) {
      LOGGER.error("--class parameter must be specified");
      return Optional.empty();
    }

    String className = words.get(classIdx.getAsInt() + 1);
    try {
      return Optional.ofNullable(Class.forName(className));
    } catch (ClassNotFoundException e) {
      LOGGER.error("Class {} not found on class path", className);
      return Optional.empty();
    }
  }
}