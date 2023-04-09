package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.CompleteInfoTradeRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoTradeRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class TradeExternalTaskRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private TradeExternalTaskRequestDtoSerializer serializer;
    @Mock
    private ImportCollectionModificationRequestDtoSerializer importCollectionModificationRequestDtoSerializer;
    @Mock
    private CompleteInfoTradeRequestDtoSerializer completeInfoTradeRequestDtoSerializer;

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ImportCollectionModificationRequestDto expectedImportCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-modification-request-dto.json").getFile(), ImportCollectionModificationRequestDto.class);
        TradeRequest importCollectionTradeRequest = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-modification-trade-request-1.json").getFile(), TradeRequest.class);
        when(importCollectionModificationRequestDtoSerializer.toDto(importCollectionTradeRequest)).thenReturn(expectedImportCollectionModificationRequestDto);

        TradeExternalTaskRequest tradeExternalTaskRequest = new TradeExternalTaskRequest();
        tradeExternalTaskRequest.setRequest(importCollectionTradeRequest);
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(tradeExternalTaskRequest);
        // Then
        assertEquals(expectedImportCollectionModificationRequestDto, result.getRequest());
        verify(completeInfoTradeRequestDtoSerializer).toDto(eq(tradeExternalTaskRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
