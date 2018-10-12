package jrocks.shell.value;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
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

@Component
public class ClassFieldsValueProvider extends ValueProviderSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassFieldsValueProvider.class);

  private final ClassPathScanner classPathScanner;

  @Autowired
  public ClassFieldsValueProvider(final ClassPathScanner classPathScanner) {this.classPathScanner = classPathScanner;}

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return getSourceClass(completionContext)
        .map(cp -> getCompletionProposals(cp, completionContext))
        .orElse(new ArrayList<>());
  }

  @VisibleForTesting
  List<CompletionProposal> getCompletionProposals(Class<?> clazz, CompletionContext completionContext) {
    final String word = completionContext.currentWordUpToCursor();
    final String lastProposal = StringUtils.substringAfterLast(word, ",").isEmpty()
        ? StringUtils.substringBeforeLast(word, ",")
        : StringUtils.substringAfterLast(word, ",");

    final List<String> alreadyExcludedFields = Arrays.asList(StringUtils.split(word, ","));

    final boolean existingPropertyName = classPathScanner.getMutableFields(clazz.getCanonicalName()).stream()
        .anyMatch(name -> name.equals(lastProposal.trim()));

    return classPathScanner.getMutableFields(clazz.getCanonicalName()).stream()
        .filter(name -> !alreadyExcludedFields.contains(name))
        .map(fieldName -> existingPropertyName
            ? new CompletionProposal(format("%s,%s", word, fieldName).replace(",,", ",").trim())
            : new CompletionProposal(fieldName))
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  Optional<Class<?>> getSourceClass(CompletionContext completionContext) throws IllegalStateException {
    final List<String> words = completionContext.getWords();
    final OptionalInt classIdx = IntStream.range(0, words.size() - 1)
        .filter(i -> words.get(i).equals("--class"))
        .findAny();

    if (!classIdx.isPresent()) {
      LOGGER.error("--class parameter must be specified");
      return Optional.empty();
    }

    final String className = words.get(classIdx.getAsInt() + 1);
    try {
      return Optional.ofNullable(Class.forName(className));
    } catch (ClassNotFoundException e) {
      LOGGER.error("Class {} not found on class path", className);
      return Optional.empty();
    }
  }
}