package jrocks.shell.config;

import jrocks.plugin.api.JRocksBean;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;

public interface MavenService extends JRocksBean {

  default String name() {
    return "maven";
  }

  List<MavenProject> loadProjects();

  class InputStreamConsumer implements Runnable {
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
