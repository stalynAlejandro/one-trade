package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionModificationRequest;
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
class CompleteInfoExportCollectionModificationRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CompleteInfoExportCollectionModificationRequestDtoSerializer serializer;
    @Mock
    private ExportCollectionModificationRequestDtoSerializer exportCollectionModificationRequestDtoSerializer;
    @Mock
    private CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionModificationRequestDto exportCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-modification-request-dto.json").getFile(), ExportCollectionModificationRequestDto.class);
        ExportCollectionModificationRequest expectedExportCollectionModificationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-modification-request.json").getFile(), ExportCollectionModificationRequest.class);

        CompleteInfoExportCollectionModificationRequestDto completeInfoExportCollectionModificationRequestDto = new CompleteInfoExportCollectionModificationRequestDto();
        completeInfoExportCollectionModificationRequestDto.setRequest(exportCollectionModificationRequestDto);

        when(exportCollectionModificationRequestDtoSerializer.toModel(exportCollectionModificationRequestDto)).thenReturn(expectedExportCollectionModificationRequest);
        // When
        CompleteInfoExportCollectionModificationRequest result = serializer.toModel(completeInfoExportCollectionModificationRequestDto);
        // Then
        assertEquals(expectedExportCollectionModificationRequest, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toModel(eq(completeInfoExportCollectionModificationRequestDto), any());
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionModificationRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionModificationRequestDto expectedExportCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-modification-request-dto.json").getFile(), ExportCollectionModificationRequestDto.class);
        ExportCollectionModificationRequest exportCollectionModificationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-modification-request.json").getFile(), ExportCollectionModificationRequest.class);

        CompleteInfoExportCollectionModificationRequest completeInfoExportCollectionModificationRequest = new CompleteInfoExportCollectionModificationRequest();
        completeInfoExportCollectionModificationRequest.setRequest(exportCollectionModificationRequest);

        when(exportCollectionModificationRequestDtoSerializer.toDto(exportCollectionModificationRequest)).thenReturn(expectedExportCollectionModificationRequestDto);
        // When
        CompleteInfoExportCollectionModificationRequestDto result = serializer.toDto(completeInfoExportCollectionModificationRequest);
        // Then
        assertEquals(expectedExportCollectionModificationRequestDto, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toDto(eq(completeInfoExportCollectionModificationRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionModificationRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
