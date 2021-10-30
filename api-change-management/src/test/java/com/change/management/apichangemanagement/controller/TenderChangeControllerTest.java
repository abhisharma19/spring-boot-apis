package com.change.management.apichangemanagement.controller;

import com.change.management.apichangemanagement.domain.BillDenominations;
import com.change.management.apichangemanagement.domain.TenderChangeCriteria;
import com.change.management.apichangemanagement.domain.TenderChangeEntity;
import com.change.management.apichangemanagement.domain.messaging.TenderChangeRequest;
import com.change.management.apichangemanagement.service.TenderChangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TenderChangeControllerTest {

    @Mock
    TenderChangeService tenderChangeService;

    @InjectMocks
    TenderChangeController tenderChangeController;

    private AutoCloseable closeable;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws IOException {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tenderChangeController).build();
    }

    @AfterEach
    void closeService() throws Exception{
        closeable.close();
    }

    @Test
    public void testTenderChange() throws Exception {
        TenderChangeRequest request = new TenderChangeRequest();
        TenderChangeCriteria criteria = new TenderChangeCriteria();
        criteria.setDenomination(BillDenominations.TWENTY);
        criteria.setAllowMaximumCoins(false);
        request.setCriteria(criteria);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(request);

        TenderChangeEntity tenderChangeEntity = new TenderChangeEntity();
        when(tenderChangeService.tenderChange(request)).thenReturn(tenderChangeEntity);

        mockMvc.perform(
                post("/change/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)).andExpect(status().is2xxSuccessful());
    }
}
