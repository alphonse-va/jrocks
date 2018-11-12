package jrocks.shell.valueprovider;

import io.github.classgraph.ClassInfo;
import jrocks.shell.ClassPathScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
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
    return scanner.getAllClassInfo()
        .filter(filterClasses())
        .map(c -> new CompletionProposal(c.getName())).collect(Collectors.toList());
  }

  private Predicate<ClassInfo> filterClasses() {
    return c -> c.getConstructorInfo().stream().anyMatch(p -> p.getParameterInfo().length == 0);
  }
}
