package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.service.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;
import uk.gov.ons.census.fwmt.outcomeservice.utility.OutcomeDefaultLookup;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Slf4j
@Service
public class OutcomeServiceImpl implements OutcomeService {

  public static final String PROCESSING_HH_OUTCOME = "PROCESSING_HH_OUTCOME";

  public static final String PROCESSING_SPG_OUTCOME = "PROCESSING_SPG_OUTCOME";

  public static final String PROCESSING_CE_OUTCOME = "PROCESSING_CE_OUTCOME";

  public static final String PROCESSING_CCS_PL_OUTCOME = "PROCESSING_CCS_PL_OUTCOME";

  public static final String PROCESSING_CCS_INT_OUTCOME = "PROCESSING_CCS_INT_OUTCOME";

  public static final String PROCESSING_NC_OUTCOME = "PROCESSING_NC_OUTCOME";

  public static final String FAILED_TO_LOOKUP_OUTCOME_CODE = "FAILED_TO_LOOKUP_OUTCOME_CODE";

  @Autowired
  private Map<String, OutcomeServiceProcessor> outcomeServiceProcessors;

  @Autowired
  private OutcomeDefaultLookup outcomeLookup;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  
  @PostConstruct
  public void listProcessors() {
	  Set<String> keySet = outcomeServiceProcessors.keySet();
	  for (String key : keySet) {
		  System.out.println(key);		
	}
  }
  
  
  @Override
  @Transactional
  public void createSpgOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "SPG",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Failed to  process SpgOutcome");
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_SPG_OUTCOME,
          "Survey type", "SPG",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "SPG");
    }
  }

  @Override
  @Transactional
  public void createCeOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "CE",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Failed to  process CeOutcome");
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CE_OUTCOME,
          "Survey type", "CE",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "CE");
    }
  }

  @Override
  @Transactional
  public void createHhOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "HH",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Failed to  process HhOutcome");
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_HH_OUTCOME,
          "Survey type", "HH",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "HH");
    }
  }

  @Override
  @Transactional
  public void createCcsPropertyListingOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "CCS PL",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Failed to  process CcsPlOutcome");
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CCS_PL_OUTCOME,
          "Survey type", "CCS PL",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "CCS PL");
    }
  }

  @Override
  @Transactional
  public void createCcsInterviewOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "CCS INT",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Failed to  process CcsIntOutcome");
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CCS_INT_OUTCOME,
          "Survey type", "CCS INT",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "CCS INT");
    }
  }

  @Override
  @Transactional
  public void createNcOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "NC",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Failed to  process NcOutcome");
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_NC_OUTCOME,
          "Survey type", "NC",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "NC");
    }
  }
}
