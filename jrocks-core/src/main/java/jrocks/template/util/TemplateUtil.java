package jrocks.template.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TemplateUtil {

  private final JavaUtil javaUtil;

  private final FileUtil fileUtil;

  private final AngularUtil angularUtil;

  @Autowired
  public TemplateUtil(JavaUtil javaUtil, FileUtil fileUtil, AngularUtil angularUtil) {
    this.javaUtil = javaUtil;
    this.fileUtil = fileUtil;
    this.angularUtil = angularUtil;
  }

  public JavaUtil getJavaUtil() {
    return javaUtil;
  }

  public FileUtil getFileUtil() {
    return fileUtil;
  }

  public AngularUtil getAngularUtil() {
    return angularUtil;
  }
}
