package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
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
class CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer serializer;
    @Mock
    private ExportCollectionAdvanceModificationRequestDtoSerializer exportCollectionAdvanceModificationRequestDtoSerializer;
    @Mock
    private CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceModificationRequestDto exportCollectionAdvanceModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-modification-request-dto.json").getFile(), ExportCollectionAdvanceModificationRequestDto.class);
        ExportCollectionAdvanceModificationRequest expectedExportCollectionAdvanceModificationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-modification-request.json").getFile(), ExportCollectionAdvanceModificationRequest.class);

        CompleteInfoExportCollectionAdvanceModificationRequestDto completeInfoExportCollectionAdvanceModificationRequestDto = new CompleteInfoExportCollectionAdvanceModificationRequestDto();
        completeInfoExportCollectionAdvanceModificationRequestDto.setRequest(exportCollectionAdvanceModificationRequestDto);

        when(exportCollectionAdvanceModificationRequestDtoSerializer.toModel(exportCollectionAdvanceModificationRequestDto)).thenReturn(expectedExportCollectionAdvanceModificationRequest);
        // When
        CompleteInfoExportCollectionAdvanceModificationRequest result = serializer.toModel(completeInfoExportCollectionAdvanceModificationRequestDto);
        // Then
        assertEquals(expectedExportCollectionAdvanceModificationRequest, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toModel(eq(completeInfoExportCollectionAdvanceModificationRequestDto), any());
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionAdvanceModificationRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceModificationRequestDto expectedExportCollectionAdvanceModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-modification-request-dto.json").getFile(), ExportCollectionAdvanceModificationRequestDto.class);
        ExportCollectionAdvanceModificationRequest exportCollectionAdvanceModificationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-modification-request.json").getFile(), ExportCollectionAdvanceModificationRequest.class);

        CompleteInfoExportCollectionAdvanceModificationRequest completeInfoExportCollectionAdvanceModificationRequest = new CompleteInfoExportCollectionAdvanceModificationRequest();
        completeInfoExportCollectionAdvanceModificationRequest.setRequest(exportCollectionAdvanceModificationRequest);

        when(exportCollectionAdvanceModificationRequestDtoSerializer.toDto(exportCollectionAdvanceModificationRequest)).thenReturn(expectedExportCollectionAdvanceModificationRequestDto);
        // When
        CompleteInfoExportCollectionAdvanceModificationRequestDto result = serializer.toDto(completeInfoExportCollectionAdvanceModificationRequest);
        // Then
        assertEquals(expectedExportCollectionAdvanceModificationRequestDto, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toDto(eq(completeInfoExportCollectionAdvanceModificationRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionAdvanceModificationRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
