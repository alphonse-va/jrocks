package jrocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.jline.JLineShellAutoConfiguration;

@SpringBootApplication(exclude = JLineShellAutoConfiguration.class, scanBasePackages = "jrocks")
public class JRocksApplication {

  public static void main(String[] args) {
    SpringApplication.run(JRocksApplication.class, args);
  }

}
