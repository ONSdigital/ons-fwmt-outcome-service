package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayDefaultCache;

import javax.transaction.Transactional;

@Component
public class SwitchCaseIdDefaultService {

    @Autowired
    private GatewayCacheDefaultService gatewayCacheService;

    @Transactional
    public String fromNcToOriginal(String caseID) {
        GatewayDefaultCache gatewayCache = gatewayCacheService.getById(caseID);
        return gatewayCache.getOriginalCaseId();
    }

    @Transactional
    public String fromIdOriginalToNc(String caseId) {
        GatewayDefaultCache gatewayCache = gatewayCacheService.getByOriginalId(caseId);
        return gatewayCache.getCaseId();
    }
}
