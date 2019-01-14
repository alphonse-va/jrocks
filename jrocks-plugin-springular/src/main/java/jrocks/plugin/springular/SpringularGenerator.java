package jrocks.plugin.springular;

import com.fizzed.rocker.Rocker;
import jrocks.plugin.api.*;
import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import jrocks.plugin.api.config.ModuleType;
import jrocks.plugin.api.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Qualifier(SpringularPlugin.LAYOUT_QUALIFIER)
public class SpringularGenerator implements PluginGenerator {

  private static final String TEMPLATES_PATH = "jrocks/plugin/springular/templates";
  private static final String REPOSITORY_ROCKER_TEMPLATE = TEMPLATES_PATH + "/backend/repository.rocker.raw";

  private static final String SERVICE_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/service/serviceTs.rocker.raw";
  private static final String DATASOURCE_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/service/datasourceTs.rocker.raw";

  private static final String COMPONENT_HTML_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/datatableComponent.rocker.html";
  private static final String COMPONENT_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/datatableComponentTs.rocker.raw";
  private static final String COMPONENT_SPEC_TS_ROCKER_TEMPLATE = TEMPLATES_PATH + "/frontend/entity/datatable/datatableComponentSpecTs.rocker.raw";

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

  @Autowired
  private ConfigService configService;

  @Autowired
  private TerminalLogger terminalLogger;

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

    String newComponentHtmlContent = Rocker.template(NEW_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String newComponentTsContent = Rocker.template(NEW_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String newComponentSpecTsContent = Rocker.template(NEW_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();

    String editComponentHtmlContent = Rocker.template(EDIT_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String editComponentTsContent = Rocker.template(EDIT_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String editComponentSpecTsContent = Rocker.template(EDIT_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();

    String deleteComponentHtmlContent = Rocker.template(DELETE_DIALOG_COMPONENT_HTML_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String deleteComponentTsContent = Rocker.template(DELETE_DIALOG_COMPONENT_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();
    String deleteComponentSpecTsContent = Rocker.template(DELETE_DIALOG_COMPONENT_SPEC_TS_ROCKER_TEMPLATE, classApi, parameter).render().toString();


    String packageName = parameter.getUserResponse(SpringularPlugin.Q_PACKAGE)
        .map(QuestionResponse::text)
        .orElse(classApi.packageName());


    String angularDir = Paths.get(getAngularModule().getBaseDirectory(), "src", "app").toFile().getAbsolutePath();

    File appModuleTsFile = Paths.get(angularDir, "app.module.ts").toFile();


    // FIXME implement something proper to import and add components as module into *module.ts

    String moduleBasePath = "entity/" + classApi.resourceName() + "-datatable/";
    importAngularModule(classApi, appModuleTsFile, moduleBasePath + classApi.resourceName() + "-datatable.component", classApi.simpleName() + "DatatableComponent");
    importAngularModule(classApi, appModuleTsFile, moduleBasePath + "new/new-" + classApi.resourceName() + "-dialog.component", "New" + classApi.simpleName() + "DialogComponent");
    importAngularModule(classApi, appModuleTsFile, moduleBasePath + "edit/edit-" + classApi.resourceName() + "-dialog.component", "Edit" + classApi.simpleName() + "DialogComponent");
    importAngularModule(classApi, appModuleTsFile, moduleBasePath + "delete/delete-" + classApi.resourceName() + "-dialog.component", "Delete" + classApi.simpleName() + "DialogComponent");


    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.declaration@", "    " + classApi.simpleName() + "DatatableComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.declaration@", "    New" + classApi.simpleName() + "DialogComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.declaration@", "    Edit" + classApi.simpleName() + "DialogComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.declaration@", "    Delete" + classApi.simpleName() + "DialogComponent,");

    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.entryComponents@", "    New" + classApi.simpleName() + "DialogComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.entryComponents@", "    Edit" + classApi.simpleName() + "DialogComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.entryComponents@", "    Delete" + classApi.simpleName() + "DialogComponent,");


    return Arrays.asList(

        new GeneratedSourceSupport()
            .setContent(repositoryContent)
            .setFilename(classApi.simpleName() + "Repository.java")
            .setPath(classApi.sourceClassPath())
            .setPackageName(packageName),

        new GeneratedSourceSupport()
            .setContent(serviceTsContent)
            .setFilename(classApi.resourceName() + ".service.ts")
            .setPath(Paths.get(angularDir, "service").toFile()),
        new GeneratedSourceSupport()
            .setContent(datasourceTsContent)
            .setFilename(classApi.resourceName() + ".datasource.ts")
            .setPath(Paths.get(angularDir, "service").toFile()),

        new GeneratedSourceSupport()
            .setContent(componentHtmlContent)
            .setFilename(classApi.resourceName() + "-datatable.component.html")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable").toFile()),
        new GeneratedSourceSupport()
            .setContent(componentTsContent)
            .setFilename(classApi.resourceName() + "-datatable.component.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable").toFile()),
        new GeneratedSourceSupport()
            .setContent(componentSpecTsContent)
            .setFilename(classApi.resourceName() + "-datatable.component.spec.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable").toFile()),


        new GeneratedSourceSupport()
            .setContent(newComponentHtmlContent)
            .setFilename("new-" + classApi.resourceName() + "-dialog.component.html")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "new").toFile()),
        new GeneratedSourceSupport()
            .setContent(newComponentTsContent)
            .setFilename("new-" + classApi.resourceName() + "-dialog.component.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "new").toFile()),
        new GeneratedSourceSupport()
            .setContent(newComponentSpecTsContent)
            .setFilename("new-" + classApi.resourceName() + "-dialog.component.spec.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "new").toFile()),

        new GeneratedSourceSupport()
            .setContent(editComponentHtmlContent)
            .setFilename("edit-" + classApi.resourceName() + "-dialog.component.html")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "edit").toFile()),
        new GeneratedSourceSupport()
            .setContent(editComponentTsContent)
            .setFilename("edit-" + classApi.resourceName() + "-dialog.component.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "edit").toFile()),
        new GeneratedSourceSupport()
            .setContent(editComponentSpecTsContent)
            .setFilename("edit-" + classApi.resourceName() + "-dialog.component.spec.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "edit").toFile()),

        new GeneratedSourceSupport()
            .setContent(deleteComponentHtmlContent)
            .setFilename("delete-" + classApi.resourceName() + "-dialog.component.html")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "delete").toFile()),
        new GeneratedSourceSupport()
            .setContent(deleteComponentTsContent)
            .setFilename("delete-" + classApi.resourceName() + "-dialog.component.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "delete").toFile()),
        new GeneratedSourceSupport()
            .setContent(deleteComponentSpecTsContent)
            .setFilename("delete-" + classApi.resourceName() + "-dialog.component.spec.ts")
            .setPath(Paths.get(angularDir, "entity", classApi.resourceName() + "-datatable", "delete").toFile())

    );
  }

  private void importAngularModule(ClassApi classApi, File appModuleTsFile, String path, String component) {
    String importTemplate = "import {%s} from \"./%s\"";
    String formattedImport = String.format(importTemplate, component, path);
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.module.import@", formattedImport);
  }

  private GeneratedSourceSupport newSource(ClassApi classApi, String content, String packageName) {
    return null;
  }

  private ModuleConfig getAngularModule() {
    return configService.getConfig().getModuleByType(ModuleType.ANGULAR)
        .orElseThrow(() -> new IllegalStateException("A module of type ANGULAR is required by this generator, please check your config."));
  }

  private ModuleConfig getSpringDataRestModule() {
    return configService.getConfig().getModuleByType(ModuleType.SPRING_DATA_REST)
        .orElseThrow(() -> new IllegalStateException("A module of type ANGULAR is required by this generator, please check your config."));
  }

  private void addContentAfterMarker(File file, String tag, String content) {
    try {
      String currentTag = "";
      for (String line : Files.lines(file.toPath()).collect(Collectors.toList())) {
        if (line.contains(tag)) {
          currentTag = tag;
        }

        if (currentTag.equals(tag) && line.contains(content.trim())) {
          terminalLogger.warning("Content _%s_ for tag _%s_ already exist into _%s_, skipped!", content, tag, file.getName());
          return;
        }
      }
    } catch (IOException e) {
      throw new SpringularPluginException(e);
    }
    try (Stream<String> lines = Files.lines(file.toPath())) {
      List<String> replaced = lines
          .map(line -> line.replace(tag, tag + "\n" + content))
          .collect(Collectors.toList());
      Files.write(file.toPath(), replaced);
    } catch (IOException e) {
      throw new SpringularPluginException(e);
    }
  }
}
