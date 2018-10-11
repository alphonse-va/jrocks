//package jrocks.shell;
//
//import jrocks.model.BeanClassInfo;
//import jrocks.model.BeanClassInfoBuilder;
//import jrocks.model.FieldClassInfoApi;
//import jrocks.model.MapperData;
//import jrocks.template.bean.dto;
//import jrocks.template.bean.mapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import picocli.CommandLine;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static org.apache.commons.lang3.StringUtils.isNotBlank;
//
//@CommandLine.Command(
//    description = "Generate dto from any bean",
//    name = "dto",
//    mixinStandardHelpOptions = true,
//    abbreviateSynopsis = true,
//    showDefaultValues = true,
//    version = "1.0")
//@Component
//public class DtoCommand extends ClassCommandUtil {
//
//  private static final Logger LOGGER = LoggerFactory.getLogger(DtoCommand.class);
//
//  @CommandLine.Option(names = {"-s", "--suffix"}, description = "Suffix to use, default [DTO]")
//  private String suffix = "DTO";
//
//  @CommandLine.Option(names = {"-rs", "--suffix-to-remove"}, description = "Remove the source class suffix")
//  private String suffixToRemove;
//
//  @CommandLine.Option(names = {"-m", "--with-mapper-class"}, description = "Generate a mapping file instead of a static factory method inside the DTO.")
//  private boolean withFactoryMethod = true;
//
//  @CommandLine.Option(names = {"-e", "--exclude-properties"}, description = "Exclude properties")
//  private List<String> excludedProperties = new ArrayList<>();
//
//  @CommandLine.Option(names = {"-i", "--include-properties"}, description = "Exclude properties")
//  private List<String> includeProperties = new ArrayList<>();
//
//  public static void main(String[] args) {
//    CommandLine.call(new DtoCommand(), args);
//  }
//
//  @Override
//  protected void execute() {
//    final BeanClassInfo<?> beanMetaData = new BeanClassInfoBuilder<>(getClazz()).build();
//
//    final List<String> beanFields = beanMetaData.getFields().stream()
//        .map(FieldClassInfoApi::fieldName)
//        .collect(Collectors.toList());
//
//    // log missing fields
//    final boolean missingIncludedField = hasMissingFields(beanFields, includeProperties, "Included field not found: {}");
//    final boolean missingExcludedField = hasMissingFields(beanFields, excludedProperties, "Excluded field not found: {}");
//    if (missingExcludedField || missingIncludedField) return;
//
//    // filters
//    if (!includeProperties.isEmpty()) {
//      LOGGER.info("Included fields: '{}'", includeProperties);
//      filterProperties(beanMetaData, f -> includeProperties.contains(f.fieldName()));
//    } else if (!excludedProperties.isEmpty()) {
//      LOGGER.info("Excluded fields: '{}'", excludedProperties);
//      filterProperties(beanMetaData, f -> !excludedProperties.contains(f.fieldName()));
//    }
//
//    final MapperData mapperData = new MapperData()
//        .setSuffix(suffix)
//        .setSuffixToRemove(suffixToRemove)
//        .setWithFactoryMethod(withFactoryMethod);
//
//    String generatedSource = dto.template(beanMetaData, mapperData).render().toString();
//    writeGeneratedFile(applySuffix(beanMetaData), generatedSource, suffix);
//
//    // generate mapper class
//    if (!withFactoryMethod) {
//      String generatedMapperSource = mapper.template(beanMetaData, mapperData).render().toString();
//      writeGeneratedFile(applySuffix(beanMetaData), generatedMapperSource, suffix + "Mapper");
//    }
//  }
//
//  private String applySuffix(BeanClassInfo<?> beanMetaData) {
//    final String input = beanMetaData.canonicalName().replaceAll("\\.", "/");
//    return isNotBlank(suffixToRemove) ? input.replaceAll(suffixToRemove + "$", suffix) : input + suffix;
//  }
//
//
//  private static boolean hasMissingFields(List<String> fields, List<String> properties, String errorMessage) {
//    final boolean[] hasMissingFields = new boolean[]{false};
//    properties.stream()
//        .filter(f -> !fields.contains(f))
//        .forEach(f -> {
//          LOGGER.error(errorMessage, f);
//          hasMissingFields[0] = true;
//        });
//    return hasMissingFields[0];
//  }
//
//  private static void filterProperties(BeanClassInfo<?> beanMetaData, Function<FieldClassInfoApi<?>, Boolean> filter) {
//    final List<FieldClassInfoApi<?>> filtered = beanMetaData.getFields().stream()
//        .filter(filter::apply)
//        .collect(Collectors.toList());
//    beanMetaData.setProperties(filtered);
//  }
//
//  @Override
//  protected Logger getLogger() {
//    return LOGGER;
//  }
//}
//
//
