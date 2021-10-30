package com.change.management.apichangemanagement.service;


import com.change.management.apichangemanagement.domain.CoinType;
import com.change.management.apichangemanagement.domain.TenderChangeCriteria;
import com.change.management.apichangemanagement.domain.TenderChangeEntity;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeRequest;
import com.change.management.apichangemanagement.exception.TenderChangeException;
import com.change.management.apichangemanagement.validation.TenderChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.change.management.apichangemanagement.constants.TenderChangeConstants.DIME_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.NICKEL_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.PENNY_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.QUARTER_COIN;
import static com.change.management.apichangemanagement.domain.CoinType.DIME;
import static com.change.management.apichangemanagement.domain.CoinType.NICKEL;
import static com.change.management.apichangemanagement.domain.CoinType.PENNY;
import static com.change.management.apichangemanagement.domain.CoinType.QUARTER;

/**
 * The type Tender change service.
 */
@Service
public class TenderChangeService implements ITenderChangeService {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(TenderChangeService.class);

    /** The coinsMap. */
    private static Map<String, Integer> coinsMap = new HashMap<String, Integer>();

    /** The pennyCount. */
    private static int pennyCount;

    /** The nickelCount. */
    private static int nickelCount;

    /** The dimeCount. */
    private static int dimeCount;

    /** The quarterCount. */
    private static int quarterCount;

    /** The tenderChangeValidator. */
    @Autowired
    private TenderChangeValidator tenderChangeValidator;

    /**
     * Instantiates a new Tender change service.
     *
     * @param penny   the penny
     * @param nickel  the nickel
     * @param dime    the dime
     * @param quarter the quarter
     */
    public TenderChangeService(@Value("${coins.penny}") int penny, @Value("${coins.nickel}") int nickel,
                               @Value("${coins.dime}") int dime, @Value("${coins.quarter}") int quarter) {
        pennyCount = penny;
        nickelCount = nickel;
        dimeCount = dime;
        quarterCount = quarter;

        coinsMap.put(PENNY.getValue(), pennyCount);
        coinsMap.put(NICKEL.getValue(), nickelCount);
        coinsMap.put(DIME.getValue(), dimeCount);
        coinsMap.put(QUARTER.getValue(), quarterCount);
    }

    /**
     * Method to tender change for the given bill.
     * @param tenderChangeRequest request object
     * @return tenderChangeEntity
     * @throws TenderChangeException tenderChangeException
     */
    @Override
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

    /**
     * Method to calculate change.
     * @param denomination bill denomination
     * @param allowMaximumCoins allowMaximumCoins
     * @return Map containing the final coins count for the given bill
     */
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

    /**
     * Overloaded method to calculate change for each coin type.
     * @param denomination bill denomination
     * @param coinType coinType
     * @return number of coins required for the given coinType
     */
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

    /**
     * Method to check if enough coins of given coin type are available for the bill.
     * @param requiredCoinCount required coin count for the given coin type
     * @param coinType coint type
     * @return true/false
     */
    private boolean checkCoinAvailability(int requiredCoinCount, String coinType) {
        boolean coinsAvailable = false;
        if (coinsMap.get(coinType) >= requiredCoinCount) {
            coinsAvailable = true;
        }
        return coinsAvailable;
    }

    /**
     * Method to get the coin denomination based on coin type.
     * @param coinType coint type
     * @return coin denomination
     */
    private double getCoinDenomination(CoinType coinType) {
        switch (coinType) {
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
