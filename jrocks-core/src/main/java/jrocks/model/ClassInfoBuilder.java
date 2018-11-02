package jrocks.model;

import io.github.classgraph.ClassRefTypeSignature;
import jrocks.plugin.api.ClassApi;


/**
 *
 * Class info builder class
 *
 * @implNote  We fall back to reflection based ClassInfo when GlassGraph doesn't fill {@code io.github.classgraph.ClassInfo}
 * for internal classes e.g {@code java.lang.Long}
 *
 */
public class ClassInfoBuilder {

  private ClassApi classInfo;

  private io.github.classgraph.ClassInfo beanClass;

  public ClassInfoBuilder(io.github.classgraph.ClassInfo beanClass) {
    this.beanClass = beanClass;
    this.classInfo = new ClassGraphClassInfo(beanClass);
  }

  public ClassApi build() {
    beanClass.getDeclaredFieldInfo().forEach(fieldInfo -> {
      io.github.classgraph.ClassInfo fieldClassInfo = ((ClassRefTypeSignature) fieldInfo.getTypeDescriptor()).getClassInfo();
      if (fieldClassInfo == null) {
        classInfo.addField(new ReflectiveFieldInfo(fieldInfo.loadClassAndGetField()));
      } else {
        classInfo.addField(new ClassGraphFieldInfo(fieldInfo));
      }
    });
    return classInfo;
  }
}