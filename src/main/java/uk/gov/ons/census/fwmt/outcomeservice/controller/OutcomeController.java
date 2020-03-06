package uk.gov.ons.census.fwmt.outcomeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPGSTANDALONE_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPG_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_JSON_CONVERSION;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Autowired
  private ObjectMapper objectMapper;

  private String outcomeType = null;

  //  @Override
  //  public ResponseEntity<Void> householdCaseOutcomeResponse(String caseId, HouseholdOutcome householdOutcome)
  //      throws GatewayException {
  //    gatewayEventManager.triggerEvent(caseId, COMET_HH_OUTCOME_RECEIVED, "transactionId",
  //        householdOutcome.getTransactionId().toString(), "Case Ref", householdOutcome.getCaseReference(),
  //        "Primary Outcome", householdOutcome.getPrimaryOutcome(), "Secondary Outcome",
  //        householdOutcome.getSecondaryOutcome());
  //    householdOutcome.setCaseId(UUID.fromString(caseId));
  //
  //    try {
  //      String householdOutcomeToQueue = objectMapper.writeValueAsString(householdOutcome);
  //      outcomeType = "Household";
  //      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(householdOutcomeToQueue, caseId, outcomeType);
  //    } catch (JsonProcessingException e) {
  //      String errorMessage = "Unable to move household outcome to pre-processing queue.";
  //      gatewayEventManager
  //          .triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION, "Case Ref",
  //              householdOutcome.getCaseReference());
  //      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
  //          errorMessage + " for case Id: " + caseId);
  //    }
  //    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  //  }
  //
  //  @Override
  //  public ResponseEntity<Void> ccsPropertyListingCaseOutcomeResponse(
  //      CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {
  //    String caseId = ccsPLOutcome.getPropertyListingCaseId().toString();
  //    gatewayEventManager
  //        .triggerEvent(caseId, COMET_CCSPL_OUTCOME_RECEIVED, "transactionId", ccsPLOutcome.getTransactionId().toString(),
  //            "Primary Outcome", ccsPLOutcome.getPrimaryOutcome(), "Secondary Outcome",
  //            ccsPLOutcome.getSecondaryOutcome());
  //
  //    try {
  //      String ccsPLOutcomeToQueue = objectMapper.writeValueAsString(ccsPLOutcome);
  //      outcomeType = "CCSPL";
  //      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(ccsPLOutcomeToQueue, caseId, outcomeType);
  //    } catch (JsonProcessingException e) {
  //      String errorMessage = "Unable to move household outcome to pre-processing queue.";
  //      gatewayEventManager.triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION);
  //      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
  //          errorMessage + " for case Id: " + caseId);
  //    }
  //    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  //  }
  //
  //  @Override
  //  public ResponseEntity<Void> ccsInterviewOutcome(String caseId, CCSInterviewOutcome ccsInterviewOutcome)
  //      throws GatewayException {
  //    gatewayEventManager.triggerEvent(caseId, COMET_CCSSI_OUTCOME_RECEIVED, "transactionId",
  //        ccsInterviewOutcome.getTransactionId().toString(), "Case Ref", ccsInterviewOutcome.getCaseReference(),
  //        "Primary Outcome", ccsInterviewOutcome.getPrimaryOutcome(), "Secondary Outcome",
  //        ccsInterviewOutcome.getSecondaryOutcome());
  //
  //    try {
  //      String ccsInterviewOutcomeToQueue = objectMapper.writeValueAsString(ccsInterviewOutcome);
  //      outcomeType = "CCSINT";
  //      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(ccsInterviewOutcomeToQueue, caseId, outcomeType);
  //    } catch (JsonProcessingException e) {
  //      String errorMessage = "Unable to move household outcome to pre-processing queue.";
  //      gatewayEventManager
  //          .triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION, "Case Ref",
  //              ccsInterviewOutcome.getCaseReference());
  //      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
  //          errorMessage + " for case Id: " + caseId);
  //    }
  //    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  //  }

  @Override
  public ResponseEntity<Void> spgOutcomeResponse(String caseId, SPGOutcome spgOutcome)
      throws GatewayException {
    gatewayEventManager.triggerEvent(caseId, COMET_CESPG_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Case Ref", spgOutcome.getCaseReference(),
        "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());

    try {
      String spgOutcomeToQueue = objectMapper.writeValueAsString(spgOutcome);
      outcomeType = "SPG";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcomeToQueue, caseId, outcomeType);
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move SPG outcome to pre-processing queue.";
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION, "Case Ref",
              spgOutcome.getCaseReference());
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          errorMessage + " for case Id: " + caseId);
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<Void> spgNewUnitAddress(SPGOutcome spgOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(String.valueOf(spgOutcome.getSiteCaseId()), COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Case Ref", spgOutcome.getCaseReference(),
        "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());

    try {
      String spgOutcomeToQueue = objectMapper.writeValueAsString(spgOutcome);
      outcomeType = "SPGUA";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcomeToQueue, String.valueOf(spgOutcome.getSiteCaseId()), outcomeType);
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move SPG Unit Address outcome to pre-processing queue.";
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), e, errorMessage, String.valueOf(spgOutcome.getSiteCaseId()), FAILED_JSON_CONVERSION, "Case Ref",
              spgOutcome.getCaseReference());
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          errorMessage + " for case Id: " + spgOutcome.getSiteCaseId());
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<Void> spgNewStandalone(String caseId, SPGOutcome spgOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseId, COMET_CESPGSTANDALONE_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Case Ref", spgOutcome.getCaseReference(),
        "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());

    try {
      String spgOutcomeToQueue = objectMapper.writeValueAsString(spgOutcome);
      outcomeType = "SPGNS";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcomeToQueue, caseId, outcomeType);
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move SPG New Standalone outcome to pre-processing queue.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION,
              "Case Ref", spgOutcome.getCaseReference());
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          errorMessage + " for case Id: " + spgOutcome.getSiteCaseId());
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
}
