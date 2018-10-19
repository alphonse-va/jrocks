package jrocks.model;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassRefTypeSignature;
import jrocks.api.ClassInfoApi;


/**
 *
 * Class info builder class
 *
 * @implNote  We fall back to reflection based ClassInfoApi when GlassGraph doesn't fill {@code io.github.classgraph.ClassInfo}
 * for internal classes e.g {@code java.lang.Long}
 *
 */
public class ClassInfoBuilder {

  private ClassInfoApi classInfoApi;

  private ClassInfo beanClass;

  public ClassInfoBuilder(ClassInfo beanClass) {
    this.beanClass = beanClass;
    this.classInfoApi = new ClassGraphClassInfo(beanClass);
  }

  public ClassInfoApi build() {
    beanClass.getDeclaredFieldInfo().forEach(fieldInfo -> {
      ClassInfo fieldClassInfo = ((ClassRefTypeSignature) fieldInfo.getTypeDescriptor()).getClassInfo();
      if (fieldClassInfo == null) {
        classInfoApi.addField(new ReflectiveFieldInfo(fieldInfo.loadClassAndGetField()));
      } else {
        classInfoApi.addField(new ClassGraphFieldInfo(fieldInfo));
      }
    });
    return classInfoApi;
  }
}