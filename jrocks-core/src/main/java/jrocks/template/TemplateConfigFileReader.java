package jrocks.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jrocks.plugin.api.template.TemplateDescriptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

@Component
class TemplateConfigFileReader {

  private ObjectMapper mapper;

  @PostConstruct
  void postConstruct() {
    mapper = new ObjectMapper(new YAMLFactory());
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Path.class, new PathSerializer());
    mapper.registerModule(module);

  }

  List<TemplateDescriptor> readAllDescriptors(File configFile) throws JRocksTemplateException {
    if (!configFile.exists()) {
      throw new JRocksTemplateException(format("Config file '%s' not found!", configFile.getAbsolutePath()));
    }
    try {
      TemplateConfigFile holder = mapper.readValue(configFile, TemplateConfigFile.class);
      return holder.getTemplates();
    } catch (IOException e) {
      throw new JRocksTemplateException(e);
    }
  }
}
