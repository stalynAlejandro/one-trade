package com.pagonxt.onetradefinance.external.backend.service.trade;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class TradeContractServiceTest {

    @Mock
    private RestTemplateWorkService restTemplateWorkService;

    @InjectMocks
    private TradeContractService tradeContractService;

    @Test
    void getCustomerImportCollections_whenPassingCustomerAndUser_thenFindTradeContracts() {
        // Given
        String customerName = "BUC-1234567";
        UserInfo userInfo = new UserInfo(new User("userId", "userName", "userType"));
        TradeContract tradeContract = new TradeContract();
        tradeContract.setCode("Code1");
        List<TradeContract> response = List.of(tradeContract);
        ControllerResponse controllerResponse = ControllerResponse.success("", response);
        when(restTemplateWorkService.findTradeContracts(eq("cli_request"), any())).thenReturn(controllerResponse);
        // When
        List<TradeContract> result = tradeContractService.getCustomerImportCollections(customerName, userInfo);
        // Then
        ArgumentCaptor<TradeContractsQuery> captor = ArgumentCaptor.forClass(TradeContractsQuery.class);
        verify(restTemplateWorkService, times(1)).findTradeContracts(eq("cli_request"), captor.capture());
        TradeContractsQuery query = captor.getValue();
        assertEquals(userInfo, query.getRequester());
        assertEquals(customerName, query.getCustomerCode());
        assertEquals("Code1", result.get(0).getCode());
    }
}
