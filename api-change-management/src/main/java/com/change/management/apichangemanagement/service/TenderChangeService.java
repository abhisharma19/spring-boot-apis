package com.change.management.apichangemanagement.service;


import com.change.management.apichangemanagement.domain.CoinType;
import com.change.management.apichangemanagement.domain.TenderChangeCriteria;
import com.change.management.apichangemanagement.domain.TenderChangeEntity;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeRequest;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeResponse;
import com.change.management.apichangemanagement.exception.TenderChangeException;
import com.change.management.apichangemanagement.validation.TenderChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.change.management.apichangemanagement.constants.TenderChangeConstants.PENNY_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.NICKEL_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.DIME_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.QUARTER_COIN;
import static com.change.management.apichangemanagement.domain.CoinType.PENNY;
import static com.change.management.apichangemanagement.domain.CoinType.NICKEL;
import static com.change.management.apichangemanagement.domain.CoinType.DIME;
import static com.change.management.apichangemanagement.domain.CoinType.QUARTER;


@Service
public class TenderChangeService {

    private static final Logger logger = LoggerFactory.getLogger(TenderChangeService.class);

    private static Map<String, Integer> coinsMap = new HashMap<String, Integer>();
    private static int pennyCountStatic;
    private static int nickelCountStatic;
    private static int dimeCountStatic;
    private static int quarterCountStatic;

    public TenderChangeService(@Value("${coins.penny}") int penny, @Value("${coins.nickel}") int nickel,
                               @Value("${coins.dime}") int dime, @Value("${coins.quarter}") int quarter) {
        pennyCountStatic = penny;
        nickelCountStatic = nickel;
        dimeCountStatic = dime;
        quarterCountStatic = quarter;

        coinsMap.put(PENNY.getValue(), pennyCountStatic);
        coinsMap.put(NICKEL.getValue(), nickelCountStatic);
        coinsMap.put(DIME.getValue(), dimeCountStatic);
        coinsMap.put(QUARTER.getValue(), quarterCountStatic);
    }

    @Autowired
    private TenderChangeValidator tenderChangeValidator;

    public TenderChangeEntity tenderChange(TenderChangeRequest tenderChangeRequest) throws TenderChangeException {
        logger.info("Calculating the change....");
        TenderChangeCriteria criteria = tenderChangeRequest.getCriteria();
        int denomination = criteria.getDenomination().getValue();
        tenderChangeValidator.validateRequestedDenomination(denomination, coinsMap);
        Map<String, Integer> requiredCoinsMap = calculateChange(denomination, criteria.isAllowMaximumCoins());
        TenderChangeEntity tenderChangeEntity = new TenderChangeEntity();
        int pennyCount = requiredCoinsMap.get(PENNY.getValue());
        int nickelCount = requiredCoinsMap.get(NICKEL.getValue());
        int dimeCount = requiredCoinsMap.get(DIME.getValue());
        int quarterCount = requiredCoinsMap.get(QUARTER.getValue());
        tenderChangeEntity.setPennyCount(pennyCount);
        tenderChangeEntity.setNickelCount(nickelCount);
        tenderChangeEntity.setDimeCount(dimeCount);
        tenderChangeEntity.setQuarterCount(quarterCount);
        logger.info("Required number of coins for the amount {} are : Penny -> {} :: Nickel -> {} :: Dime -> {} :: Quarter -> {}",
                denomination, pennyCount, nickelCount, dimeCount, quarterCount);
        return tenderChangeEntity;
    }

    private synchronized Map<String, Integer> calculateChange(double denomination, boolean allowMaximumCoins) {
        int pennyCoinsRequired = 0;
        int nickelCoinsRequired = 0;
        int dimeCoinsRequired = 0;
        int quarterCoinsRequired = 0;

        if (allowMaximumCoins) {
            pennyCoinsRequired = calculateChange(denomination, PENNY);
            denomination = denomination - (pennyCoinsRequired * PENNY_COIN);

            nickelCoinsRequired = calculateChange(denomination, NICKEL);
            denomination = denomination - (nickelCoinsRequired * NICKEL_COIN);

            dimeCoinsRequired = calculateChange(denomination, DIME);
            denomination = denomination - (dimeCoinsRequired * DIME_COIN);

            quarterCoinsRequired = calculateChange(denomination, QUARTER);
        } else {
            quarterCoinsRequired = calculateChange(denomination, QUARTER);
            denomination = denomination - (quarterCoinsRequired * QUARTER_COIN);

            dimeCoinsRequired = calculateChange(denomination, DIME);
            denomination = denomination - (dimeCoinsRequired * DIME_COIN);

            nickelCoinsRequired = calculateChange(denomination, NICKEL);
            denomination = denomination - (nickelCoinsRequired * NICKEL_COIN);

            pennyCoinsRequired = calculateChange(denomination - (nickelCoinsRequired * NICKEL_COIN), PENNY);
        }

        Map<String, Integer> requiredCoins = new HashMap<>();
        requiredCoins.put(PENNY.getValue(), pennyCoinsRequired);
        requiredCoins.put(NICKEL.getValue(), nickelCoinsRequired);
        requiredCoins.put(DIME.getValue(), dimeCoinsRequired);
        requiredCoins.put(QUARTER.getValue(), quarterCoinsRequired);

        coinsMap.put(PENNY.getValue(), coinsMap.get(PENNY.getValue()) - pennyCoinsRequired);
        coinsMap.put(NICKEL.getValue(), coinsMap.get(NICKEL.getValue()) - nickelCoinsRequired);
        coinsMap.put(DIME.getValue(), coinsMap.get(DIME.getValue()) - dimeCoinsRequired);
        coinsMap.put(QUARTER.getValue(), coinsMap.get(QUARTER.getValue()) - quarterCoinsRequired);
        return requiredCoins;
    }

    private int calculateChange(double denomination, CoinType coinType) {
        if (denomination <= 0) {
            return 0;
        }
        double coinDenomination = getCoinDenomination(coinType);
        int requiredCoins = (int) (denomination / coinDenomination);
        if (!checkCoinAvailability(requiredCoins, coinType.getValue())) {
            requiredCoins = coinsMap.get(coinType.getValue());
        }
        return requiredCoins;
    }

    private boolean checkCoinAvailability(int requiredCoinCount, String coinType) {
        boolean coinsAvailable = false;
        if (coinsMap.get(coinType) >= requiredCoinCount) {
            coinsAvailable = true;
        }
        return coinsAvailable;
    }

    private double getCoinDenomination(CoinType coinType) {
        switch(coinType) {
            case PENNY:
                return PENNY_COIN;
            case NICKEL:
                return NICKEL_COIN;
            case DIME:
                return DIME_COIN;
            case QUARTER:
                return QUARTER_COIN;
            default:
                return QUARTER_COIN;
        }
    }
}
