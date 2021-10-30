package com.change.management.apichangemanagement.service;

import com.change.management.apichangemanagement.domain.BillDenominations;
import com.change.management.apichangemanagement.domain.TenderChangeCriteria;
import com.change.management.apichangemanagement.domain.TenderChangeEntity;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeRequest;
import com.change.management.apichangemanagement.validation.TenderChangeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


public class TenderChangeServiceTest {

    @Mock
    private TenderChangeValidator tenderChangeValidator;

    @InjectMocks
    private TenderChangeService tenderChangeService = new TenderChangeService(100, 100, 100, 100);

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() throws IOException {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception{
        closeable.close();
    }

    @Test
    public void testTenderChangeWithLeastCoins() {
        TenderChangeRequest request = new TenderChangeRequest();
        TenderChangeCriteria criteria = new TenderChangeCriteria();
        criteria.setDenomination(BillDenominations.TWENTY);
        criteria.setAllowMaximumCoins(false);
        request.setCriteria(criteria);
        doNothing().when(tenderChangeValidator).validateRequestedDenomination(any(Integer.class), any(Map.class));
        TenderChangeEntity tenderChangeEntity = tenderChangeService.tenderChange(request);
        assertEquals(0, tenderChangeEntity.getPennyCount());
        assertEquals(0, tenderChangeEntity.getNickelCount());
        assertEquals(0, tenderChangeEntity.getDimeCount());
        assertEquals(80, tenderChangeEntity.getQuarterCount());
    }

    @Test
    public void testTenderChangeWithMaximumCoins() {
        TenderChangeRequest request = new TenderChangeRequest();
        TenderChangeCriteria criteria = new TenderChangeCriteria();
        criteria.setDenomination(BillDenominations.TWENTY);
        criteria.setAllowMaximumCoins(true);
        request.setCriteria(criteria);
        doNothing().when(tenderChangeValidator).validateRequestedDenomination(any(Integer.class), any(Map.class));
        TenderChangeEntity tenderChangeEntity = tenderChangeService.tenderChange(request);
        assertEquals(100, tenderChangeEntity.getPennyCount());
        assertEquals(100, tenderChangeEntity.getNickelCount());
        assertEquals(100, tenderChangeEntity.getDimeCount());
        assertEquals(16, tenderChangeEntity.getQuarterCount());
    }
}
