package jrocks.shell.config;

import com.google.common.annotations.VisibleForTesting;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public final class MavenProjectUtil {

  private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");

  public List<MavenProject> loadProjects() {
    MavenXpp3Reader mavenReader = new MavenXpp3Reader();
    List<String> effectivePoms = grabMavenEffectivePoms();
    try {
      List<MavenProject> result = new ArrayList<>();
      for (String effectivePom : effectivePoms) {
        Model model = mavenReader.read(new StringReader(effectivePom));
        model.setPomFile(new File("pom.xml"));
        result.add(new MavenProject(model));
      }
      return result;
    } catch (IOException | XmlPullParserException e) {
      throw new IllegalStateException("Error while reading followed effective maven pom:\n\n" + effectivePoms, e);
    }
  }

  private List<String> grabMavenEffectivePoms() {
    ProcessBuilder builder = new ProcessBuilder();
    if (IS_WINDOWS) {
      builder.command("mvn.exe", "help:effective-pom");
    } else {
      builder.command("mvn", "help:effective-pom");
    }
    try {
      Process process = builder.start();
      StringWriter result = new StringWriter();
      Runnable streamConsumer = new InputStreamConsumer(process.getInputStream(), result::write);
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.submit(streamConsumer);
      int exitCode = process.waitFor();
      assert exitCode == 0;
      executorService.shutdown();
      return effectivePomToListOfPoms(result.toString());
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException("Error while executing 'mvn help:effective-pom' command", e);
    }
  }

  @VisibleForTesting
  List<String> effectivePomToListOfPoms(String poms) {
    return Stream.of(poms.split("<!-- ====================================================================== -->"))
        .filter(block -> block.contains("<project xmlns"))
        .collect(Collectors.toList());
  }

  private static class InputStreamConsumer implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    InputStreamConsumer(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
  }
}
