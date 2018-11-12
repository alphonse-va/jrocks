package jrocks.shell.valueprovider;

import jrocks.shell.command.PluginsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneratorValueProvider extends ValueProviderSupport {

  private PluginsHolder pluginHolder;

  @Autowired
  public GeneratorValueProvider(PluginsHolder pluginHolder) {
    this.pluginHolder = pluginHolder;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter methodParameter, CompletionContext completionContext, String[] strings) {
    return pluginHolder.getCurrentPlugin().generators().stream()
        .map(generator -> new CompletionProposal(generator.name())).collect(Collectors.toList());
  }
}
