package jrocks.shell.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AllClassValueProvider extends ValueProviderSupport {

  private final ClassPathScanner scanner;

  @Autowired
  public AllClassValueProvider(ClassPathScanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return scanner.getAllClassesWithZeroArgsConstructor().stream().map(CompletionProposal::new).collect(Collectors.toList());
  }
}
