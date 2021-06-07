package uk.gov.ons.census.fwmt.outcomeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableRetry
@ComponentScan({"uk.gov.ons.census.fwmt.outcomeservice","uk.gov.ons.census.fwmt.outcomeservice", "uk.gov.ons.fwmt.outcomeprocessors", "uk.gov.ons.fwmt.outcomeprocessors.data", "uk.gov.ons.census.fwmt.events",
    "uk.gov.ons.ctp.integration.common.product", "uk.gov.census.ffa.storage.utils","uk.gov.ons.fwmt.outcometest"})
//@EnableJpaRepositories("uk.gov.ons.fwmt.outcomeprocessors")
public class Application {

  public static final String APPLICATION_NAME = "FWMT Gateway - Outcome Service";

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

}