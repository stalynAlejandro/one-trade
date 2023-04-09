package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.riskline.RiskLineGateway;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineResponse;
import com.pagonxt.onetradefinance.integrations.apis.riskline.serializer.SantanderRiskLineSerializer;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@UnitTest
class SantanderRiskLineResponseServiceTest {

    @Mock
    RiskLineGateway riskLineGateway;

    @Mock
    SantanderRiskLineSerializer santanderRiskLineSerializer;

    @InjectMocks
    SantanderRiskLineService santanderRiskLineService;

    @Test
    void getCustomerRiskLines_isOk_returnsListOfRiskLine() throws IOException {
        //Given
        List<SantanderRiskLineListResponse> santanderRiskLineResponse = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        santanderRiskLineResponse.add(mapper.readValue(new ClassPathResource("api-data/santander/risk-line-response.json").getFile(), SantanderRiskLineListResponse.class));

        SantanderRiskLineSerializer serializer = new SantanderRiskLineSerializer();
        List<com.pagonxt.onetradefinance.integrations.model.RiskLine> riskLines = serializer.toModel(santanderRiskLineResponse.get(0));

        doReturn(santanderRiskLineResponse.get(0)).when(riskLineGateway).getRiskLineByCostumerId(new RiskLineQuery());
        doReturn(riskLines).when(santanderRiskLineSerializer).toModel((SantanderRiskLineListResponse) any());
        //When
        List<com.pagonxt.onetradefinance.integrations.model.RiskLine> result = santanderRiskLineService.getCustomerRiskLines(new RiskLineQuery());
        //Then
        assertEquals(riskLines, result);

    }

    @Test
    void getRiskLineById_isOk_returnsListOfRiskLine() throws IOException {
        //Given
        List<SantanderRiskLineResponse> santanderRiskLine = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        santanderRiskLine.add(mapper.readValue(new ClassPathResource("api-data/santander/risk-line-byId-response.json").getFile(), SantanderRiskLineResponse.class));
        SantanderRiskLineSerializer serializer = new SantanderRiskLineSerializer();
        com.pagonxt.onetradefinance.integrations.model.RiskLine riskLines = serializer.toModel(santanderRiskLine.get(0));

        doReturn(santanderRiskLine.get(0)).when(riskLineGateway).getRiskLineById(new RiskLineQuery());
        doReturn(riskLines).when(santanderRiskLineSerializer).toModel((SantanderRiskLineResponse) any());
        //When
        com.pagonxt.onetradefinance.integrations.model.RiskLine result = santanderRiskLineService.getRiskLineById(new RiskLineQuery());
        //Then
        assertEquals(riskLines, result);
    }
}