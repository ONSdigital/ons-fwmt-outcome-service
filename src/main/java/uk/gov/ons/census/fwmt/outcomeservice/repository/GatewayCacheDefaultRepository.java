package uk.gov.ons.census.fwmt.outcomeservice.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayDefaultCache;

@Repository
public interface GatewayCacheDefaultRepository extends JpaRepository<GatewayDefaultCache, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  GatewayDefaultCache findByCaseId(String caseId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  GatewayDefaultCache findByOriginalCaseId(String caseId);

}
