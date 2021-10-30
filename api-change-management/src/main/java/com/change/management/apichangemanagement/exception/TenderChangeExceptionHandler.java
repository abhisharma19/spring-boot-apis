package com.change.management.apichangemanagement.exception;

import com.change.management.apichangemanagement.domain.exception.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class TenderChangeExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TenderChangeExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Error> handleBadRequest(BadRequestException badRequestException) {
        return new ResponseEntity<>(badRequestException.getError(), badRequestException.getError().getCode());
    }
}
