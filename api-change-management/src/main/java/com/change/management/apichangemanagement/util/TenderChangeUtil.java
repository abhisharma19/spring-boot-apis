package com.change.management.apichangemanagement.util;

import org.springframework.stereotype.Component;

import java.util.Map;

import static com.change.management.apichangemanagement.constants.TenderChangeConstants.DIME_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.NICKEL_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.PENNY_COIN;
import static com.change.management.apichangemanagement.constants.TenderChangeConstants.QUARTER_COIN;
import static com.change.management.apichangemanagement.domain.CoinType.DIME;
import static com.change.management.apichangemanagement.domain.CoinType.NICKEL;
import static com.change.management.apichangemanagement.domain.CoinType.PENNY;
import static com.change.management.apichangemanagement.domain.CoinType.QUARTER;

@Component
public class TenderChangeUtil {

    public double calculateAvailableAmount(Map<String, Integer> coinsMap) {
        double pennyAmount = coinsMap.get(PENNY.getValue()) * PENNY_COIN;
        double nickelAmount = coinsMap.get(NICKEL.getValue()) * NICKEL_COIN;
        double dimeAmount = coinsMap.get(DIME.getValue()) * DIME_COIN;
        double quarterAmount = coinsMap.get(QUARTER.getValue()) * QUARTER_COIN;

        double totalAvailableAmount = pennyAmount + nickelAmount + dimeAmount + quarterAmount;
        return totalAvailableAmount;
    }
}
