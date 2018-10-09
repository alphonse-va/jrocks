//package jrocks.shell;
//
//import com.google.common.reflect.ClassPath;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import picocli.CommandLine;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//import static java.lang.String.format;
//
//public final class ClassCommandUtil {
//
//
//  Logger LOGGER = LoggerFactory.getLogger(ClassCommandUtil.class);
//
//  boolean writeGeneratedFile(String relativePath, String generatedSource, String className) {
//    final String sourceDirectory = JRocksConfigHolder.getConfig(JRocksConfigHolder.JRocksConfig.TARGET_SOURCE_DIRECTORY)
//        .orElseThrow(() -> new IllegalStateException(JRocksConfigHolder.JRocksConfig.TARGET_SOURCE_DIRECTORY.getKey() + " not found!"));
//
//    final String destPath = String.format("%s/%s/%s%s.java",
//        JRocksConfigHolder.PROJECT_BASE_DIRECTORY,
//        sourceDirectory,
//        relativePath,
//        className);
//
//    try {
//      final Path path = Paths.get(destPath);
//      if (path.toFile().exists() && !isOverwrite()) {
//        LOGGER.error("'{}' file exists, please use --overwrite if you want to", path.toString());
//        return true;
//      }
//      path.toFile().createNewFile();
//      final Path savedFile = Files.write(path, generatedSource.getBytes());
//      LOGGER.info("[builder] - '{}' class generated with success.\n\nSource: {}\nGenerated: {}\n", savedFile.getFileName(), getClazz().getCanonicalName() + ".java", savedFile);
//    } catch (IOException e) {
//      throw new IllegalStateException(e.getLocalizedMessage(), e);
//    }
//
//    return false;
//  }
//
//  private static String getCanonicalNameFromFile(File file) {
//    final String name = file.getName().replaceAll("\\.java", "");
//    String classContent;
//    try {
//      classContent = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
//    } catch (IOException e) {
//      throw new IllegalStateException(e.getLocalizedMessage(), e);
//    }
//
//    final Pattern packagePattern = Pattern.compile("^[ |\\t|package]+([\\t| |]+)(?<package>[a-z|A-Z|.]+)", Pattern.DOTALL);
//    final Matcher matcher = packagePattern.matcher(classContent);
//    final String packageName;
//    if (matcher.find()) packageName = matcher.group("package");
//    else throw new IllegalArgumentException(format("Package not found in '%s' source file", file.getAbsolutePath()));
//
//    return String.format("%s.%s", packageName, name);
//  }
//
//  static class ProjectClassArrayList extends ArrayList<String> {
//    public ProjectClassArrayList() throws IOException {
//      super(ClassPath.from(ProjectClassArrayList.class.getClassLoader()).getTopLevelClassesRecursive(JRocksConfigHolder.getConfig(JRocksConfigHolder.JRocksConfig.PROJECT_BASE_PACKAGE).orElse("")).stream().map(ClassPath.ClassInfo::getName).collect(Collectors.toList()));
//    }
//  }
//}
