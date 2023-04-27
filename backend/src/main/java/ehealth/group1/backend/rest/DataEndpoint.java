package ehealth.group1.backend.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ehealth.group1.backend.config.FhirHapiDeserializer;
import ehealth.group1.backend.customfhirstructures.CustomObservation;
import ehealth.group1.backend.service.ECGService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(path = "/data")
public class DataEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  ECGService ecgService;

  public DataEndpoint(ECGService ecgService){
    this.ecgService = ecgService;
  }

  /*@PostMapping("/receive")
  @ResponseStatus(HttpStatus.OK)
  public void receiveData(@RequestBody String data) {
    LOGGER.info("Received ecg data from client");
    LOGGER.debug("\n" + "Data:\n" + data + "\n");
    ecgService.processECG(data);
  }*/

  @RequestMapping(value="/receive", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
  //@JsonDeserialize(using = FhirHapiDeserializer.class)
  @ResponseStatus(HttpStatus.OK)
  public void receiveObservation(@RequestBody CustomObservation obs) {
    LOGGER.info("Received ecg data from client (obs)");
    ecgService.processECG(obs);
  }

  @PostMapping("/receive")
  @ResponseStatus(HttpStatus.OK)
  public void receiveJson(@RequestBody String data) {
    LOGGER.info("Received ecg data from client");
    ecgService.processECG(data);
  }

  // For testing purposes, reflects the body of the incoming post message back to the sender
  @PostMapping("/reflect")
  @ResponseStatus(HttpStatus.OK)
  public String reflect(@RequestBody String data) {
    LOGGER.info("Reflected data:\n\n" + data + "\n");
    return data;
  }

  /*@PostMapping("/entityTest")
  @ResponseStatus(HttpStatus.OK)
  public String testEntityConversion(@RequestBody String data) {
    LOGGER.info("Received ecg data from client");
    return ecgService.convertToEntity(data).getJSONRepresentation();
  }*/
}
