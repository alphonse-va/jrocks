package jrocks.shell.valueproviders;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class ClassFieldsValueProvider extends ValueProviderSupport {

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return getSourceClass(completionContext)
        .map(this::getCompletionProposals)
        .orElse(new ArrayList<>());
  }

  @VisibleForTesting
  List<CompletionProposal> getCompletionProposals(Class<?> clazz) {
    return Stream.of(FieldUtils.getAllFields(clazz))
        .map(Field::getName)
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  Optional<Class<?>> getSourceClass(CompletionContext completionContext) throws IllegalStateException {
    final List<String> words = completionContext.getWords();
    final OptionalInt classIdx = IntStream.range(0, words.size() - 1)
        .filter(i -> words.get(i).equals("--class"))
        .findAny();

    if (!classIdx.isPresent()) {
      System.err.println("--class parameter must be specified");
      return Optional.empty();
    }

    final String className = words.get(classIdx.getAsInt() + 1);
    try {
      return Optional.ofNullable(Class.forName(className));
    } catch (ClassNotFoundException e) {
      System.err.printf("\nClass '%s' not found on class path\n", className);
      return Optional.empty();
    }
  }
}