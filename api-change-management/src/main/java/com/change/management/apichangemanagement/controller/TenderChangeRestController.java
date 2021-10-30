package com.change.management.apichangemanagement.controller;

import com.change.management.apichangemanagement.domain.TenderChangeEntity;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeRequest;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeResponse;
import com.change.management.apichangemanagement.service.ITenderChangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The TenderChangeRestController
 */
@RestController
@RequestMapping("/change")
public class TenderChangeRestController {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger((TenderChangeRestController.class));

    /** The tenderChangeService. */
    @Autowired
    private ITenderChangeService tenderChangeService;

    /**
     * Method to tender change for the given bill amount.
     * @param tenderChangeRequest request containing criteria for the requested bill
     * @return ResponseEntity<TenderChangeResponse>
     */
    @PostMapping(value = {"/calculate"}, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TenderChangeResponse> tenderChange(@RequestBody TenderChangeRequest tenderChangeRequest) {
        StopWatch watch = new StopWatch();
        watch.start();

        try {
            TenderChangeEntity tenderChangeEntity = tenderChangeService.tenderChange(tenderChangeRequest);
            TenderChangeResponse response = new TenderChangeResponse(tenderChangeEntity);
            ResponseEntity<TenderChangeResponse> finalResponse = new ResponseEntity<>(response, HttpStatus.OK);
            return finalResponse;
        } finally {
            watch.stop();
            logger.info("Time taken to complete tender change request : {} ms", watch.getTotalTimeMillis());
        }
    }
}
