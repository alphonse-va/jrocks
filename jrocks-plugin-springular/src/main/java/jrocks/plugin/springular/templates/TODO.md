
# TODO

## Backend Repository

- Fix package name
- Support for query with int, Integer, double, boolean, BigDecimal
- Repository method name question

## Frontend
### Service

- Support for filter with int, Integer, double, boolean, BigDecimal

### Nice to have
- configurable backend package layout

# Generator TODO

# Template and resource processing

## Config file 
Template descriptor API

## Config file examples

**Simple file**
````
TemplateDescriptor
    sourcePath: template/test.component.scss
    targetPath: "app/component/" + bean.resourceName()
    targetFilename: bean.resourceName() + "-test.component.scss"
````

**Class template**
````
TemplateDescriptor
    sourcePath: template/repository.rocker.raw
````
**TypeScript template**
````
TemplateDescriptor
    sourcePath: template/repositoryTs.rocker.raw
    targetPath: "app/component/" + bean.resourceName()
    targetFilename: bean.resourceName() + "-test.component.scss"
````
## Expected Result
````
ContentDescriptor
    sourcePath: /template/test.component.scss
        
    // apply bean.* param.all on TemplateDescriptor
    
    targetPath: /app/component/matrix
    targetFilename: matrix-test.component.scss
    
    // file2string
    
    sourceContent: [content of test.component.scss]
````

**Class**
````
ContentDescriptor
    sourcePath: eg. /template/repository.rocker.raw
    
    // expected
    sourceContent: template result
    
    // we need a target class + given package to ContentDescriptor enricher
    targetPath: Example.java
    targetFilename: /src/main/java/test

````

## Implementation

### Interpret properties
We choose Groovy to interpret property values by giving bean and param as script context.
````
  e.g. bean.resourceName() + "-test.component.scss" become -> example-test.component.scss
````

#### _Expression_ for additional template parameters
As we also need to given more parameter to some templates, we should provide a config param field to convert a template to string.
  
  e.g. the repository package name

````
Rocker.template(template, bean, parameter, [ additional parameter HERE ]).render().toString()
````

##### Default template _expression_
As we don't want to have boiled plate config properties, we should provide a default template expression
````
Rocker.template(template, bean, parameter).render().toString()
````

#### Java _targetPath_ and _targetFileName_ expression
As we don't want to have boiled plate config properties, we should also provide a default expression for java templates
````
TemplateDescriptor
    targetPath: javaUtils.targetPathFromContent(bean.sourceClassPath())
    targetFilename: javaUtils.targetFilenameFromContent()
````
### Post write _script_
As we have to modify some other files, we have to at least provide a post write optional properties

Property name: postWriteScript

We should bind bean and $param but also Template descriptor properties to the script

e.g. importing typescript component to angular module.ts
````
    angularUtils.importComponent(targetPath + "/" + $targetFile, "../app.module.ts");

    module
    fileUtils.addContentAfterMarker("../app.module.ts", "@jrocks.angular.declaration@", );

    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.entryComponents@", "    New" + classApi.simpleName() + "DialogComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.entryComponents@", "    Edit" + classApi.simpleName() + "DialogComponent,");
    addContentAfterMarker(appModuleTsFile, "@jrocks.angular.entryComponents@", "    Delete" + classApi.simpleName() + "DialogComponent,");
```` 

