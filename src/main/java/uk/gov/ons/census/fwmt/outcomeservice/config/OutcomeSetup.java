package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.utility.OutcomeLookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class OutcomeSetup {

  @Autowired
  private ResourceLoader resourceLoader;

  @Value(value = "${outcomeservice.outcomeCodeLookup.path}")
  private String outcomeCodeLookupPath;

  @Bean
  public OutcomeLookup buildOutcomeLookup() throws GatewayException {
    String line;
    Resource resource = resourceLoader.getResource(outcomeCodeLookupPath);

    OutcomeLookup outcomeLookup = new OutcomeLookup();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream(), UTF_8))) {
      while ((line = in.readLine()) != null) {
        String[] lookup = line.split("\\|");
        outcomeLookup.add(lookup[0], lookup[1].split(","));
      }
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, e, "Cannot process outcome lookup");
    }
    return outcomeLookup;
  }
}
