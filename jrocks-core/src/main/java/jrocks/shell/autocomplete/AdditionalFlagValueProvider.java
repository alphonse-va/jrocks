package jrocks.shell.autocomplete;

import jrocks.shell.generator.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AdditionalFlagValueProvider extends ValueProviderSupport {

  private final List<TemplateGenerator> templates;

  @Autowired
  public AdditionalFlagValueProvider(List<TemplateGenerator> templates) {
    this.templates = templates;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    String builderName = getBuilderName(completionContext);
    return templates.stream()
        .filter(t -> t.getName().equals(builderName))
        .map(TemplateGenerator::getAdditionalFlags)
        .flatMap(Collection::stream)
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }

  private String getBuilderName(CompletionContext completionContext) throws IllegalStateException {
    List<String> words = completionContext.getWords();
    OptionalInt classIdx = IntStream.range(0, words.size() - 1)
        .filter(i -> words.get(i).equals("--generator"))
        .findAny();
    if (!classIdx.isPresent()) {
      return null;
    }
    return words.get(classIdx.getAsInt() + 1);
  }
}

