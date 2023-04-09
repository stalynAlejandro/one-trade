package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceCancellationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceCancellationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
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
class CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer serializer;
    @Mock
    private ExportCollectionAdvanceCancellationRequestDtoSerializer exportCollectionAdvanceCancellationRequestDtoSerializer;
    @Mock
    private CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceCancellationRequestDto exportCollectionAdvanceCancellationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-cancellation-request-dto.json").getFile(), ExportCollectionAdvanceCancellationRequestDto.class);
        ExportCollectionAdvanceCancellationRequest expectedExportCollectionAdvanceCancellationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-cancellation-request.json").getFile(), ExportCollectionAdvanceCancellationRequest.class);

        CompleteInfoExportCollectionAdvanceCancellationRequestDto completeInfoExportCollectionAdvanceCancellationRequestDto = new CompleteInfoExportCollectionAdvanceCancellationRequestDto();
        completeInfoExportCollectionAdvanceCancellationRequestDto.setRequest(exportCollectionAdvanceCancellationRequestDto);

        when(exportCollectionAdvanceCancellationRequestDtoSerializer.toModel(exportCollectionAdvanceCancellationRequestDto)).thenReturn(expectedExportCollectionAdvanceCancellationRequest);
        // When
        CompleteInfoExportCollectionAdvanceCancellationRequest result = serializer.toModel(completeInfoExportCollectionAdvanceCancellationRequestDto);
        // Then
        assertEquals(expectedExportCollectionAdvanceCancellationRequest, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toModel(eq(completeInfoExportCollectionAdvanceCancellationRequestDto), any());
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionAdvanceCancellationRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceCancellationRequestDto expectedExportCollectionAdvanceCancellationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-cancellation-request-dto.json").getFile(), ExportCollectionAdvanceCancellationRequestDto.class);
        ExportCollectionAdvanceCancellationRequest exportCollectionAdvanceCancellationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-cancellation-request.json").getFile(), ExportCollectionAdvanceCancellationRequest.class);

        CompleteInfoExportCollectionAdvanceCancellationRequest completeInfoExportCollectionAdvanceCancellationRequest = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        completeInfoExportCollectionAdvanceCancellationRequest.setRequest(exportCollectionAdvanceCancellationRequest);

        when(exportCollectionAdvanceCancellationRequestDtoSerializer.toDto(exportCollectionAdvanceCancellationRequest)).thenReturn(expectedExportCollectionAdvanceCancellationRequestDto);
        // When
        CompleteInfoExportCollectionAdvanceCancellationRequestDto result = serializer.toDto(completeInfoExportCollectionAdvanceCancellationRequest);
        // Then
        assertEquals(expectedExportCollectionAdvanceCancellationRequestDto, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toDto(eq(completeInfoExportCollectionAdvanceCancellationRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionAdvanceCancellationRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
