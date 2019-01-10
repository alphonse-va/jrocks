package jrocks.plugin.springular;

import com.fizzed.rocker.Rocker;
import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
@Qualifier(SpringularPlugin.LAYOUT_QUALIFIER)
public class SpringularGenerator implements PluginGenerator {

  private static final String TEMPLATES_PATH = "jrocks/plugin/springular/templates";
  private static final String REPOSITORY_ROCKER_TEMPLATE = TEMPLATES_PATH + "/backend/repository.rocker.raw";

  private static final String SERVICE_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/service/serviceTs.rocker.raw";
  private static final String DATASOURCE_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/service/datasourceTs.rocker.raw";

  private static final String COMPONENT_HTML_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/component.rocker.html";
  private static final String COMPONENT_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/componentTs.rocker.raw";
  private static final String COMPONENT_SPEC_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/componentSpecTs.rocker.raw";

  private static final String NEW_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/newDialog/newDialogComponent.rocker.html";
  private static final String NEW_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/newDialog/newDialogComponentTs.rocker.raw";
  private static final String NEW_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/newDialog/newDialogComponentSpecTs.rocker.raw";

  private static final String EDIT_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/edit/editDialogComponent.rocker.html";
  private static final String EDIT_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/edit/editDialogComponentTs.rocker.raw";
  private static final String EDIT_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/edit/editDialogComponentSpecTs.rocker.raw";

  private static final String DELETE_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/delete/deleteDialogComponent.rocker.html";
  private static final String DELETE_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/delete/deleteDialogComponentTs.rocker.raw";
  private static final String DELETE_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/delete/deleteDialogComponentSpecTs.rocker.raw";

  @Value("${jrocks.version}")
  private String version;

  @PostConstruct
  public void postConstruct() {
  }

  @Override
  public String description() {
    return "Springular generator";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {

    String repositoryContent = Rocker.template(REPOSITORY_ROCKER_TEMPLATE, classApi, parameter).render().toString();


    String serviceTsContent = Rocker.template(SERVICE_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String datasourceTsContent = Rocker.template(DATASOURCE_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();

    String componentHtmlContent = Rocker.template(COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String componentTsContent = Rocker.template(COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String componentSpecTsContent = Rocker.template(COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();

    String newDialogComponentHtmlContent = Rocker.template(NEW_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String newDialogComponentTsContent = Rocker.template(NEW_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String newDialogComponentSpecTsContent = Rocker.template(NEW_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();

    String editComponentHtmlContent = Rocker.template(EDIT_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String editComponentTsContent = Rocker.template(EDIT_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String editComponentSpecTsContent = Rocker.template(EDIT_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();

    String deleteComponentHtmlContent = Rocker.template(DELETE_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String deleteComponentTsContent = Rocker.template(DELETE_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String deleteComponentSpecTsContent = Rocker.template(DELETE_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();


    String packageName = parameter.getUserResponse(SpringularPlugin.Q_PACKAGE)
        .map(QuestionResponse::text)
        .orElse(classApi.packageName());

    return Arrays.asList(newSource(classApi, repositoryContent, packageName),
        newSource(classApi, serviceTsContent, packageName),
        newSource(classApi, datasourceTsContent, packageName),

        newSource(classApi, componentHtmlContent, packageName),
        newSource(classApi, componentTsContent, packageName),
        newSource(classApi, componentSpecTsContent, packageName),

        newSource(classApi, newDialogComponentHtmlContent, packageName),
        newSource(classApi, newDialogComponentTsContent, packageName),
        newSource(classApi, newDialogComponentSpecTsContent, packageName),


        newSource(classApi, editComponentHtmlContent, packageName),
        newSource(classApi, editComponentTsContent, packageName),
        newSource(classApi, editComponentSpecTsContent, packageName),

        newSource(classApi, deleteComponentHtmlContent, packageName),
        newSource(classApi, deleteComponentTsContent, packageName),
        newSource(classApi, deleteComponentSpecTsContent, packageName)

    );
  }

  private GeneratedSourceSupport newSource(ClassApi classApi, String datasourceTsContent, String packageName) {
    return new GeneratedSourceSupport()
        .setContent(datasourceTsContent)
        .setFilename(classApi.simpleName())
        .setPath(classApi.sourceClassPath())
        .setPackageName(packageName);
  }
}
