package com.backend.orderhere;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class OrderHereApplication {
  private static final Logger logger = LoggerFactory.getLogger(OrderHereApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(OrderHereApplication.class, args);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(Environment env) {
    return event -> {
      String contextPath = env.getProperty("server.servlet.context-path", "");
      String host = "localhost";
      int port = env.getProperty("server.port", Integer.class, 8080);
      String swaggerUrl = String.format("http://%s:%d%s/swagger-ui/index.html", host, port, contextPath);
      logger.info("Swagger UI URL: {}", swaggerUrl);
    };
  }

}
