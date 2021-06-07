package uk.gov.ons.census.fwmt.outcomeservice.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class OutcomeDefaultLookup {

  private final Map<String, String[]> outcomeCodeMap = new HashMap<>();

  public String[] getLookup(String outcomeCode) {

    return outcomeCodeMap.get(outcomeCode);
  }

  public void add(String productCode, String[] processorNames) {
    outcomeCodeMap.put(productCode, processorNames);
  }

}

