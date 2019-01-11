package jrocks.shell.valueprovider;

import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import jrocks.shell.JRocksShellException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
public class PackageValueProvider extends ValueProviderSupport {

  private final ConfigService projectConfig;

  @Autowired
  public PackageValueProvider(ConfigService configService) {
    this.projectConfig = configService;
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    List<CompletionProposal> proposals = new ArrayList<>();
    projectConfig.getConfig().getModules().stream()
        .map(ModuleConfig::getOutputDirectory)
        .forEach(outputDirectory -> addPackageProposals(proposals, outputDirectory));
    return proposals;
  }

  private void addPackageProposals(List<CompletionProposal> proposals, String outputDirectory) {
    try {
      Path outputDirectoryPath = Paths.get(outputDirectory);
      Files.walk(outputDirectoryPath).forEach(path -> {
        if (path.toFile().isDirectory()) {
          String relativePath = path.toFile().getAbsolutePath().replace(outputDirectory, "");
          relativePath = relativePath.isEmpty() ? "" : relativePath.substring(1);
          try {
            String proposal = relativePath.replaceAll(File.separator, ".");
            if (!proposal.isEmpty()) {
              boolean containClasses = Files.walk(path).anyMatch(p -> p.toFile().getName().endsWith(".class"));
              boolean hasNoFiles = Files.walk(path).noneMatch(p -> p.toFile().isFile());
              if (containClasses || hasNoFiles) {
                proposals.add(new CompletionProposal(proposal));
              }
            }
          } catch (IOException e) {
            throw new JRocksShellException(e.getMessage(), e);
          }
        }
      });
    } catch (IOException e) {
      throw new JRocksShellException(format("Error while walking '%s' file.", outputDirectory), e);
    }
  }
}
