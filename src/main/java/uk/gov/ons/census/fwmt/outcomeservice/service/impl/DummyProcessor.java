package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.service.OutcomeServiceProcessor;

@Component("DUMMY_PROCESSOR")
public class DummyProcessor implements OutcomeServiceProcessor {

    @Override
    public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
        UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();
        return caseId;
    }
}

