package com.change.management.apichangemanagement.validation;

import com.change.management.apichangemanagement.domain.exception.Error;
import com.change.management.apichangemanagement.exception.BadRequestException;
import com.change.management.apichangemanagement.exception.TenderChangeException;
import com.change.management.apichangemanagement.util.TenderChangeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

import static com.change.management.apichangemanagement.constants.TenderChangeConstants.INSUFFICIENT_COINS;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.INVALID_AMOUNT;

@Component
public class TenderChangeValidator {

    private static final Logger logger = LoggerFactory.getLogger(TenderChangeValidator.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TenderChangeUtil tenderChangeUtil;

    public void validateRequestedDenomination(Integer denomination, Map<String, Integer> coinsMap) throws TenderChangeException {
        if (denomination < 0) {
            throwTenderChangeException(INVALID_AMOUNT, HttpStatus.BAD_REQUEST);
        } else {
            Double totalAvailableAmount = tenderChangeUtil.calculateAvailableAmount(coinsMap);
            if (denomination > totalAvailableAmount) {
                throwTenderChangeException(INSUFFICIENT_COINS, HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void throwTenderChangeException(String errorCode, HttpStatus status) throws TenderChangeException {
        Object[] messageParams = null;
        String message = messageSource.getMessage(errorCode, messageParams, Locale.US);
        Error error = new Error(HttpStatus.BAD_REQUEST, message);
        throw new BadRequestException(error, message);
    }
}
