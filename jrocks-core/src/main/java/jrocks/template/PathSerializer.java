package jrocks.template;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jrocks.plugin.api.JRocksApiException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


class PathSerializer extends StdDeserializer<Path> {

  PathSerializer() {
    this(null);
  }

  private PathSerializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Path deserialize(JsonParser jp, DeserializationContext ctxt) {
    try {
      JsonNode node = jp.getCodec().readTree(jp);
      String path = node.asText();
      return Paths.get(path);
    } catch (IOException e) {
      throw new JRocksApiException(e);
    }
  }
}
