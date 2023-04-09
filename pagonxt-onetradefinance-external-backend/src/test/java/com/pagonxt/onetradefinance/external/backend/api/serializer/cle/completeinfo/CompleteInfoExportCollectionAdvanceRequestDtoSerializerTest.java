package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionAdvanceRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
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
class CompleteInfoExportCollectionAdvanceRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CompleteInfoExportCollectionAdvanceRequestDtoSerializer serializer;
    @Mock
    private ExportCollectionAdvanceRequestDtoSerializer exportCollectionAdvanceRequestDtoSerializer;
    @Mock
    private CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceRequestDto exportCollectionAdvanceRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-request-dto.json").getFile(), ExportCollectionAdvanceRequestDto.class);
        ExportCollectionAdvanceRequest expectedExportCollectionAdvanceRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-request.json").getFile(), ExportCollectionAdvanceRequest.class);

        CompleteInfoExportCollectionAdvanceRequestDto completeInfoExportCollectionAdvanceRequestDto = new CompleteInfoExportCollectionAdvanceRequestDto();
        completeInfoExportCollectionAdvanceRequestDto.setRequest(exportCollectionAdvanceRequestDto);

        when(exportCollectionAdvanceRequestDtoSerializer.toModel(exportCollectionAdvanceRequestDto)).thenReturn(expectedExportCollectionAdvanceRequest);
        // When
        CompleteInfoExportCollectionAdvanceRequest result = serializer.toModel(completeInfoExportCollectionAdvanceRequestDto);
        // Then
        assertEquals(expectedExportCollectionAdvanceRequest, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toModel(eq(completeInfoExportCollectionAdvanceRequestDto), any());
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionAdvanceRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceRequestDto expectedExportCollectionAdvanceRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-request-dto.json").getFile(), ExportCollectionAdvanceRequestDto.class);
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-request.json").getFile(), ExportCollectionAdvanceRequest.class);

        CompleteInfoExportCollectionAdvanceRequest completeInfoExportCollectionAdvanceRequest = new CompleteInfoExportCollectionAdvanceRequest();
        completeInfoExportCollectionAdvanceRequest.setRequest(exportCollectionAdvanceRequest);

        when(exportCollectionAdvanceRequestDtoSerializer.toDto(exportCollectionAdvanceRequest)).thenReturn(expectedExportCollectionAdvanceRequestDto);
        // When
        CompleteInfoExportCollectionAdvanceRequestDto result = serializer.toDto(completeInfoExportCollectionAdvanceRequest);
        // Then
        assertEquals(expectedExportCollectionAdvanceRequestDto, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toDto(eq(completeInfoExportCollectionAdvanceRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionAdvanceRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
