package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayDefaultCache;
import uk.gov.ons.census.fwmt.outcomeservice.repository.GatewayCacheDefaultRepository;

/**
 * This class is bare-bones because it's a simple connector between the rest of the code and the caching implementation
 * Please don't subvert this class by touching the GatewayCacheRepository
 * If we ever change from a database to redis, this class will form the breaking point
 */

@Slf4j
@Service
public class GatewayCacheDefaultService {

  @Autowired
  private GatewayCacheDefaultRepository repository;

  public GatewayDefaultCache getById(String caseId) {
    return repository.findByCaseId(caseId);
  }

  public GatewayDefaultCache getByOriginalId(String caseId) {
    return repository.findByOriginalCaseId(caseId);
  }

  public void save(GatewayDefaultCache cache) {
    repository.save(cache);
  }

}
