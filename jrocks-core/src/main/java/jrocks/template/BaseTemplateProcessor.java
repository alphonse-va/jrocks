package jrocks.template;

import jrocks.JRocksBaseException;
import jrocks.plugin.api.shell.TerminalLogger;
import jrocks.plugin.api.template.ResultDescriptor;
import jrocks.plugin.api.template.TemplateContext;
import jrocks.plugin.api.template.TemplateDescriptor;
import jrocks.plugin.api.template.TemplateProcessor;
import jrocks.template.util.TemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class BaseTemplateProcessor implements TemplateProcessor {

  @Value("#{environment.SPRINGULAR_TEMPLATE_DIR}")
  private String springularTemplateDir;

  private final TemplateConfigFileReader configFileReader;

  private final ResultDescriptorWriter descriptorWriter;

  private final TerminalLogger terminalLogger;

  private final TemplateUtil templateUtil;

  @Autowired
  public BaseTemplateProcessor(TemplateConfigFileReader configFileReader, TerminalLogger terminalLogger, ResultDescriptorWriter descriptorWriter, TemplateUtil templateUtil) {
    this.configFileReader = configFileReader;
    this.terminalLogger = terminalLogger;
    this.descriptorWriter = descriptorWriter;
    this.templateUtil = templateUtil;
  }

  @Override
  public List<ResultDescriptor> process(TemplateContext context, File pluginConfigFile) {
    Objects.requireNonNull(context, "context required");
    Objects.requireNonNull(pluginConfigFile, "pluginConfigFile required");

    ArrayList<ResultDescriptor> results = new ArrayList<>();
    configFileReader.readAllDescriptors(pluginConfigFile)
        .forEach(descriptor -> {

          ResultDescriptor resultDescriptor = new ResultDescriptor()
              .setContext(context)
              .setDescriptor(descriptor)
              .setFile(evaluateFile(context, descriptor));

          if (descriptor.isRocker()) {
            resultDescriptor.setContent(evaluateContent(context, descriptor, descriptor.getTemplateExpression()));
          } else {
            resultDescriptor.setContent(pathToContent(Paths.get(springularTemplateDir, descriptor.getSourcePath())));
          }

          results.add(resultDescriptor);

          // write file
          descriptorWriter.write(resultDescriptor);

          // post write processing
          if (StringUtils.isNotBlank(descriptor.getPostWriteExpression())) {
            evaluateScript(context, descriptor, resultDescriptor, descriptor.getPostWriteExpression());
          }
          Path script = resultDescriptor.getTemplateDescriptor().getPostWriteScript();
          if (script != null && script.toFile().exists()) {
            evaluatePostScript(context, descriptor, script);
          }

          terminalLogger.verbose(resultDescriptor.toString());
        });
    return results;
  }

  private String pathToContent(Path path) {
    try {
      return String.join(File.separator, Files.readAllLines(path));
    } catch (IOException e) {
      throw new JRocksTemplateException("Error while reading content: " + path.toFile().getAbsolutePath(), e);
    }
  }

  private void evaluatePostScript(TemplateContext context, TemplateDescriptor descriptor, Path scriptPath) {
    try {
      String script = String.join("", Files.readAllLines(scriptPath));
      evaluateExpression(context, descriptor, script);
    } catch (IOException e) {
      throw new JRocksTemplateException("Error while reading script: " + scriptPath.toFile().getAbsolutePath(), e);
    }
  }

  private String evaluateContent(TemplateContext context, TemplateDescriptor descriptor, String expression) {
    String rockerImport = "import com.fizzed.rocker.Rocker;\n";
    if (isEmpty(expression)) expression = "Rocker.template(template, bean, parameter).render().toString()";
    return evaluateExpression(context, descriptor, rockerImport + expression);
  }

  private File evaluateFile(TemplateContext context, TemplateDescriptor descriptor) {
    String path = evaluateExpression(context, descriptor, descriptor.getTargetPathExpression());
    String filename = evaluateExpression(context, descriptor, descriptor.getTargetFilenameExpression());
    return Paths.get(path, filename).toFile();
  }

  private String evaluateExpression(TemplateContext context, TemplateDescriptor descriptor, String expression) {
    ScriptEngine engine = buildScriptEngine(context, descriptor);
    try {
      return (String) engine.eval(expression);
    } catch (ScriptException e) {
      throw new JRocksBaseException(String.format("%n Error:%n%s%n%nExpression:%n%s%n%n%s%n%n%s",
          e.getMessage(), expression, descriptor, context), e);
    }
  }

  private void evaluateScript(TemplateContext context, TemplateDescriptor descriptor, ResultDescriptor resultDescriptor, String expression) {
    ScriptEngine engine = buildScriptEngine(context, descriptor);

    engine.put("targetFile", resultDescriptor.getFile().getAbsolutePath());

    try {
      engine.eval(expression);
    } catch (ScriptException e) {
      throw new JRocksBaseException(String.format("%n Error:%n%s%n%nScript:%n%s%n%n%s%n%n%s",
          e.getMessage(), expression, descriptor, context), e);
    }
  }

  /**
   * TODO: Document available template context beans
   */
  private ScriptEngine buildScriptEngine(TemplateContext context, TemplateDescriptor descriptor) {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");

    engine.put("fileUtil", templateUtil.getFileUtil());
    engine.put("javaUtil", templateUtil.getJavaUtil());
    engine.put("angularUtil", templateUtil.getAngularUtil());

    engine.put("bean", context.bean());
    engine.put("parameter", context.parameter());
    engine.put("template", descriptor.getSourcePath());

    if (context.pluginContext() != null) {
      context.pluginContext().forEach(engine::put);
    }
    return engine;
  }
}
