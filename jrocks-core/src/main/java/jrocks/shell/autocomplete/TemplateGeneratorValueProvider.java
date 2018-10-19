package jrocks.shell.autocomplete;

import jrocks.shell.generator.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateGeneratorValueProvider extends ValueProviderSupport {

  private final List<TemplateGenerator> templates;

  @Autowired
  public TemplateGeneratorValueProvider(List<TemplateGenerator> templates) {this.templates = templates;}

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return templates.stream()
        .map(TemplateGenerator::getName)
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }
}

