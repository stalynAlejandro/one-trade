package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionAdvanceRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceRequestDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceRequestDtoSerializer
 * @see CompleteInfoPagoNxtRequestDtoSerializer
 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoExportCollectionAdvanceRequestDtoSerializer {

    /**
     * class attributes
     */
    private final ExportCollectionAdvanceRequestDtoSerializer serializer;
    private final CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    /**
     * Class Constructor
     * @param serializer ExportCollectionAdvanceRequestDtoSerializer object
     * @param completeInfoPagoNxtRequestDtoSerializer CompleteInfoPagoNxtRequestDtoSerializer object
     */
    public CompleteInfoExportCollectionAdvanceRequestDtoSerializer
        (ExportCollectionAdvanceRequestDtoSerializer serializer,
         CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer) {
        this.serializer = serializer;
        this.completeInfoPagoNxtRequestDtoSerializer = completeInfoPagoNxtRequestDtoSerializer;
    }

    /**
     * This method converts data from a entity class to a DTO class
     * @param completeInfoRequest a entity class
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest
     * @return null, if completeInfoRequest doesn't have data, or a DTO object
     */
    public CompleteInfoExportCollectionAdvanceRequestDto toDto
            (CompleteInfoExportCollectionAdvanceRequest completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        CompleteInfoExportCollectionAdvanceRequestDto completeInfoRequestDto =
                new CompleteInfoExportCollectionAdvanceRequestDto();
        completeInfoRequestDto.setRequest(serializer.toDto(completeInfoRequest.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toDto(completeInfoRequest, completeInfoRequestDto);
        return completeInfoRequestDto;
    }

    /**
     * This method converts data from A DTO class to an entity class
     * @param completeInfoRequestDto a DTO class
     * @see CompleteInfoExportCollectionAdvanceRequestDto
     * @return null, if completeInfoRequestDto doesn't have data, or a entity object
     */
    public CompleteInfoExportCollectionAdvanceRequest toModel
            (CompleteInfoExportCollectionAdvanceRequestDto completeInfoRequestDto) {
        if(completeInfoRequestDto == null) {
            return null;
        }
        CompleteInfoExportCollectionAdvanceRequest completeInfoRequest =
                new CompleteInfoExportCollectionAdvanceRequest();
        completeInfoRequest.setRequest(serializer.toModel(completeInfoRequestDto.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toModel(completeInfoRequestDto, completeInfoRequest);
        return completeInfoRequest;
    }
}
