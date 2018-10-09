/*
package jrocks.shell;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.utils.ReaderFactory;
import org.apache.maven.shared.utils.introspection.ReflectionValueExtractor;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MavenInfoCollector {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenInfoCollector.class);

  private Model model;

  public MavenInfoCollector(File pomFile) {
    init(pomFile);
  }

  private final MavenInfoCollector init(File pomFile) {

    */
/*
        project.build.sourceDirectory
    project.build.scriptSourceDirectory
    project.build.testSourceDirectory
    project.build.outputDirectory
    project.build.testOutputDirectory
    project.build.directory
     *//*

    MavenXpp3Reader reader = new MavenXpp3Reader();
    try {
      model = reader.read(ReaderFactory.newXmlReader(pomFile));
    } catch (IOException | XmlPullParserException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
    MavenProject project = new MavenProject(model);
    project.setFile(pomFile);
    project.getBuild();

    model = project.getModel();
    return this;
  }

  public String getSourceDirectory() {
    return Objects.toString(evaluateProperty("project.build.sourceDirectory"));
  }

  private Object  evaluateProperty(String property) {
    try {
      return ReflectionValueExtractor.evaluate(property,model);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }
}
*/
