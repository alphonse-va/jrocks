package jrocks.shell.autocomplete;

import jrocks.shell.command.CurrentPluginHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LayoutValueProvider extends ValueProviderSupport {

  private CurrentPluginHolder pluginHolder;

  @Autowired
  public LayoutValueProvider(CurrentPluginHolder pluginHolder) {
    this.pluginHolder = pluginHolder;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter methodParameter, CompletionContext completionContext, String[] strings) {
    return pluginHolder.getCurrentCommand().layouts().stream()
        .map(l -> new CompletionProposal(l.name())).collect(Collectors.toList());
  }
}
