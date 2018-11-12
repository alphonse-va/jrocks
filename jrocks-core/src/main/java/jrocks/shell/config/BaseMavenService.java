package jrocks.shell.config;

import com.google.common.annotations.VisibleForTesting;
import jrocks.shell.JRocksShellException;
import jrocks.shell.TerminalLogger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Component
public final class BaseMavenService implements MavenService {

  @Autowired
  private TerminalLogger logger;

  private static final String EFFECTIVE_POM = "help:effective-pom";
  private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");

  @Override
  public List<MavenProject> loadProjects() {
    MavenXpp3Reader mavenReader = new MavenXpp3Reader();
    List<String> effectivePoms = buildMavenEffectivePoms();
    try {
      List<MavenProject> result = new ArrayList<>();
      for (String effectivePom : effectivePoms) {
        Model model = mavenReader.read(new StringReader(effectivePom));
        model.setPomFile(new File("pom.xml"));
        result.add(new MavenProject(model));
        logger.info(this,"configure _%s:%s:%s_", model.getGroupId(), model.getArtifactId(), model.getVersion());
      }
      return result;
    } catch (IOException | XmlPullParserException e) {
      throw new JRocksShellException("Error while reading followed effective pom:\n\n" + effectivePoms, e);
    }
  }

  private List<String> buildMavenEffectivePoms() {
    ProcessBuilder builder = new ProcessBuilder();
    if (IS_WINDOWS) {
      builder.command("mvn.exe", EFFECTIVE_POM);
    } else {
      builder.command("mvn", EFFECTIVE_POM);
    }
    try {
      Process process = builder.start();
      StringWriter result = new StringWriter();
      Runnable streamConsumer = new InputStreamConsumer(process.getInputStream(), result::write);
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.submit(streamConsumer);
      int exitCode = process.waitFor();
      if (exitCode != 0) throw new AssertionError();
      executorService.shutdown();
      return splitPoms(result.toString());
    } catch (IOException | InterruptedException e) {
      throw new JRocksShellException(format("Error while executing 'mvn %s' command", EFFECTIVE_POM), e);
    }
  }

  @VisibleForTesting
  List<String> splitPoms(String poms) {
    return Stream.of(poms.split("<!-- =+ -->"))
        .filter(block -> block.contains("<project xmlns"))
        .collect(Collectors.toList());
  }
}
