package jrocks.shell.autocomplete;

import com.google.common.annotations.VisibleForTesting;
import jrocks.shell.TerminalLogger;
import jrocks.shell.generator.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AdditionalFlagValueProvider extends ValueProviderSupport {

  private final List<TemplateGenerator> templates;

  private TerminalLogger logger;


  @Autowired
  public AdditionalFlagValueProvider(List<TemplateGenerator> templates, TerminalLogger logger) {
    this.templates = templates;
    this.logger = logger;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return templates.stream()
        .filter(t -> {
          Optional<String> builderName = getBuilderName(completionContext);
          return builderName.isPresent() && t.getName().equals(builderName.get());
        })
        .map(TemplateGenerator::getAddtionalFlags)
        .flatMap(Collection::stream)
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  Optional<String> getBuilderName(CompletionContext completionContext) throws IllegalStateException {
    List<String> words = completionContext.getWords();
    OptionalInt classIdx = IntStream.range(0, words.size() - 1)
        .filter(i -> words.get(i).equals("--name"))
        .findAny();

    if (!classIdx.isPresent()) {
      logger.error("--name parameter must be specified");
      return Optional.empty();
    }
    String builderName = words.get(classIdx.getAsInt() + 1);
    return Optional.ofNullable(builderName);
  }
}

