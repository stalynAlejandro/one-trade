package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceCancellationRequestDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceCancellationRequestDtoSerializer
 * @see CompleteInfoPagoNxtRequestDtoSerializer

 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer {

    /**
     * class attributes
     */
    private final ExportCollectionAdvanceCancellationRequestDtoSerializer serializer;
    private final CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    /**
     * Class Constructor
     * @param serializer ExportCollectionAdvanceCancellationRequestDtoSerializer object
     * @param completeInfoPagoNxtRequestDtoSerializer CompleteInfoPagoNxtRequestDtoSerializer object
     */
    public CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer
            (ExportCollectionAdvanceCancellationRequestDtoSerializer serializer,
             CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer) {
        this.serializer = serializer;
        this.completeInfoPagoNxtRequestDtoSerializer = completeInfoPagoNxtRequestDtoSerializer;
    }

    /**
     * This method converts data from an entity class to a DTO class
     * @param completeInfoRequest an entity class
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest
     * @return null, if completeInfoRequest doesn't have data, or a DTO object
     */
    public CompleteInfoExportCollectionAdvanceCancellationRequestDto toDto
            (CompleteInfoExportCollectionAdvanceCancellationRequest completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        CompleteInfoExportCollectionAdvanceCancellationRequestDto completeInfoRequestDto =
                new CompleteInfoExportCollectionAdvanceCancellationRequestDto();
        completeInfoRequestDto.setRequest(serializer.toDto(completeInfoRequest.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toDto(completeInfoRequest, completeInfoRequestDto);
        return completeInfoRequestDto;
    }

    /**
     * This method converts data from A DTO class to an entity class
     * @param completeInfoRequestDto a DTO class
     * @see CompleteInfoExportCollectionAdvanceCancellationRequestDto
     * @return null, if completeInfoRequestDto doesn't have data, or a entity object
     */
    public CompleteInfoExportCollectionAdvanceCancellationRequest toModel
            (CompleteInfoExportCollectionAdvanceCancellationRequestDto completeInfoRequestDto) {
        if(completeInfoRequestDto == null) {
            return null;
        }
        CompleteInfoExportCollectionAdvanceCancellationRequest completeInfoRequest =
                new CompleteInfoExportCollectionAdvanceCancellationRequest();
        completeInfoRequest.setRequest(serializer.toModel(completeInfoRequestDto.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toModel(completeInfoRequestDto, completeInfoRequest);
        return completeInfoRequest;
    }
}
