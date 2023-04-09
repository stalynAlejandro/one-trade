package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionOtherOperationsRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionOtherOperationsRequestDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionOtherOperationsRequestDtoSerializer
 * @see CompleteInfoPagoNxtRequestDtoSerializer
 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer {

    /**
     * class attributes
     */
    private final ExportCollectionOtherOperationsRequestDtoSerializer serializer;
    private final CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    /**
     * Class Constructor
     * @param serializer ExportCollectionOtherOperationsRequestDtoSerializer object
     * @param completeInfoPagoNxtRequestDtoSerializer CompleteInfoPagoNxtRequestDtoSerializer object
     */
    public CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer
    (ExportCollectionOtherOperationsRequestDtoSerializer serializer,
     CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer) {
        this.serializer = serializer;
        this.completeInfoPagoNxtRequestDtoSerializer = completeInfoPagoNxtRequestDtoSerializer;
    }

    /**
     * This method converts data from a entity class to a DTO class
     * @param completeInfoRequest a entity class
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest
     * @return null, if completeInfoRequest doesn't have data, or a DTO object
     */
    public CompleteInfoExportCollectionOtherOperationsRequestDto toDto
            (CompleteInfoExportCollectionOtherOperationsRequest completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        CompleteInfoExportCollectionOtherOperationsRequestDto completeInfoRequestDto =
                new CompleteInfoExportCollectionOtherOperationsRequestDto();
        completeInfoRequestDto.setRequest(serializer.toDto(completeInfoRequest.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toDto(completeInfoRequest, completeInfoRequestDto);
        return completeInfoRequestDto;
    }

    /**
     * This method converts data from A DTO class to an entity class
     * @param completeInfoRequestDto a DTO class
     * @see CompleteInfoExportCollectionOtherOperationsRequestDto
     * @return null, if completeInfoRequestDto doesn't have data, or a entity object
     */
    public CompleteInfoExportCollectionOtherOperationsRequest toModel
            (CompleteInfoExportCollectionOtherOperationsRequestDto completeInfoRequestDto) {
        if(completeInfoRequestDto == null) {
            return null;
        }
        CompleteInfoExportCollectionOtherOperationsRequest completeInfoRequest =
                new CompleteInfoExportCollectionOtherOperationsRequest();
        completeInfoRequest.setRequest(serializer.toModel(completeInfoRequestDto.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toModel(completeInfoRequestDto, completeInfoRequest);
        return completeInfoRequest;
    }
}
