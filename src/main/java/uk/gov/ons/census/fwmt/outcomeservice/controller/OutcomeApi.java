package uk.gov.ons.census.fwmt.outcomeservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

@Api(value = "FWMT Census Outcome Service", description = "Operations pertaining to receiving outcomes from COMET")
@RestController
public interface OutcomeApi {

  @ApiOperation(value = "Post a household survey outcome to the FWMT Gateway", response = HouseholdOutcome.class)
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Case Outcome received", response = HouseholdOutcome.class)})
  @RequestMapping(value = "/householdOutcome/{caseId}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<HouseholdOutcome> householdCaseOutcomeResponse(
      @PathVariable String caseId, @RequestBody HouseholdOutcome householdOutcome) throws GatewayException;

  @ApiOperation(value = "Post a CCS Property Listing outcome to the FWMT Gateway", response = CCSPropertyListingOutcome.class)
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Case Outcome received", response = CCSPropertyListingOutcome.class)})
  @RequestMapping(value = "/CCSPropertyListingOutcome",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<CCSPropertyListingOutcome> ccsPropertyListingCaseOutcomeResponse(
      @RequestBody CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException;

  @ApiOperation(value = "Post a CCS Interview outcome to the FWMT Gateway", response = CCSInterviewOutcome.class)
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Case Outcome received", response = CCSInterviewOutcome.class)})
  @RequestMapping(value = "/CCSInterviewOutcome/{caseId}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<CCSInterviewOutcome> ccsInterviewOutcome(
      @PathVariable String caseId, @RequestBody CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException;
}