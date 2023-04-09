package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionOtherOperationsRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionOtherOperationsRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionOtherOperationsRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
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
class CompleteInfoExportCollectionOtherOperationsRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer serializer;
    @Mock
    private ExportCollectionOtherOperationsRequestDtoSerializer exportCollectionOtherOperationsRequestDtoSerializer;
    @Mock
    private CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionOtherOperationsRequestDto exportCollectionOtherOperationsRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-other-operations-request-dto.json").getFile(), ExportCollectionOtherOperationsRequestDto.class);
        ExportCollectionOtherOperationsRequest expectedExportCollectionOtherOperationsRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-other-operations-request.json").getFile(), ExportCollectionOtherOperationsRequest.class);

        CompleteInfoExportCollectionOtherOperationsRequestDto completeInfoExportCollectionOtherOperationsRequestDto = new CompleteInfoExportCollectionOtherOperationsRequestDto();
        completeInfoExportCollectionOtherOperationsRequestDto.setRequest(exportCollectionOtherOperationsRequestDto);

        when(exportCollectionOtherOperationsRequestDtoSerializer.toModel(exportCollectionOtherOperationsRequestDto)).thenReturn(expectedExportCollectionOtherOperationsRequest);
        // When
        CompleteInfoExportCollectionOtherOperationsRequest result = serializer.toModel(completeInfoExportCollectionOtherOperationsRequestDto);
        // Then
        assertEquals(expectedExportCollectionOtherOperationsRequest, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toModel(eq(completeInfoExportCollectionOtherOperationsRequestDto), any());
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionOtherOperationsRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionOtherOperationsRequestDto expectedExportCollectionOtherOperationsRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-other-operations-request-dto.json").getFile(), ExportCollectionOtherOperationsRequestDto.class);
        ExportCollectionOtherOperationsRequest exportCollectionOtherOperationsRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-other-operations-request.json").getFile(), ExportCollectionOtherOperationsRequest.class);

        CompleteInfoExportCollectionOtherOperationsRequest completeInfoExportCollectionOtherOperationsRequest = new CompleteInfoExportCollectionOtherOperationsRequest();
        completeInfoExportCollectionOtherOperationsRequest.setRequest(exportCollectionOtherOperationsRequest);

        when(exportCollectionOtherOperationsRequestDtoSerializer.toDto(exportCollectionOtherOperationsRequest)).thenReturn(expectedExportCollectionOtherOperationsRequestDto);
        // When
        CompleteInfoExportCollectionOtherOperationsRequestDto result = serializer.toDto(completeInfoExportCollectionOtherOperationsRequest);
        // Then
        assertEquals(expectedExportCollectionOtherOperationsRequestDto, result.getRequest());
        verify(completeInfoPagoNxtRequestDtoSerializer).toDto(eq(completeInfoExportCollectionOtherOperationsRequest), any());
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CompleteInfoExportCollectionOtherOperationsRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}

