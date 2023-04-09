package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
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
class CompleteInfoExportCollectionRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CompleteInfoExportCollectionRequestDtoSerializer serializer;
    @Mock
    private ExportCollectionRequestDtoSerializer exportCollectionRequestDtoSerializer;
    @Mock
    private CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionRequestDto exportCollectionRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-request-dto.json").getFile(), ExportCollectionRequestDto.class);
        ExportCollectionRequest expectedExportCollectionRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-request.json").getFile(), ExportCollectionRequest.class);

        CompleteInfoExportCollectionRequestDto completeInfoExportCollectionRequestDto = new CompleteInfoExportCollectionRequestDto();
        completeInfoExportCollectionRequestDto.setRequest(exportCollectionRequestDto);

        when(exportCollectionRequestDtoSerializer.toModel(exportCollectionRequestDto)).thenReturn(expectedExportCollectionRequest);
        // When
        CompleteInfoExportCollectionRequest result = serializer.toModel(completeInfoExportCollectionRequestDto);
        // Then
        assertEquals(expectedExportCollectionRequest, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toModel(eq(completeInfoExportCollectionRequestDto), any());
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionRequestDto expectedExportCollectionRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-request-dto.json").getFile(), ExportCollectionRequestDto.class);
        ExportCollectionRequest exportCollectionRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-request.json").getFile(), ExportCollectionRequest.class);

        CompleteInfoExportCollectionRequest completeInfoExportCollectionRequest = new CompleteInfoExportCollectionRequest();
        completeInfoExportCollectionRequest.setRequest(exportCollectionRequest);

        when(exportCollectionRequestDtoSerializer.toDto(exportCollectionRequest)).thenReturn(expectedExportCollectionRequestDto);
        // When
        CompleteInfoExportCollectionRequestDto result = serializer.toDto(completeInfoExportCollectionRequest);
        // Then
        assertEquals(expectedExportCollectionRequestDto, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toDto(eq(completeInfoExportCollectionRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
