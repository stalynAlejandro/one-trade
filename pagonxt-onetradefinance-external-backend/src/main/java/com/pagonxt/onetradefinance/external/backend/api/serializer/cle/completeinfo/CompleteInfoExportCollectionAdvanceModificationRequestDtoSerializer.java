package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceModificationRequestDtoSerializer
 * @see CompleteInfoPagoNxtRequestDtoSerializer
 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer {

    /**
     * class attributes
     */
    private final ExportCollectionAdvanceModificationRequestDtoSerializer serializer;
    private final CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    /**
     * Class Constructor
     * @param serializer ExportCollectionAdvanceModificationRequestDtoSerializer object
     * @param completeInfoPagoNxtRequestDtoSerializer CompleteInfoPagoNxtRequestDtoSerializer object
     */
    public CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer
            (ExportCollectionAdvanceModificationRequestDtoSerializer serializer,
             CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer) {
        this.serializer = serializer;
        this.completeInfoPagoNxtRequestDtoSerializer = completeInfoPagoNxtRequestDtoSerializer;
    }

    /**
     * This method converts data from a entity class to a DTO class
     * @param completeInfoRequest an entity class
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest
     * @return null, if completeInfoRequest doesn't have data, or a DTO object
     */
    public CompleteInfoExportCollectionAdvanceModificationRequestDto toDto
            (CompleteInfoExportCollectionAdvanceModificationRequest completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        CompleteInfoExportCollectionAdvanceModificationRequestDto completeInfoRequestDto =
                new CompleteInfoExportCollectionAdvanceModificationRequestDto();
        completeInfoRequestDto.setRequest(serializer.toDto(completeInfoRequest.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toDto(completeInfoRequest, completeInfoRequestDto);
        return completeInfoRequestDto;
    }

    /**
     * This method converts data from A DTO class to an entity class
     * @param completeInfoRequestDto a DTO class
     * @see CompleteInfoExportCollectionAdvanceModificationRequestDto
     * @return null, if completeInfoRequestDto doesn't have data, or a entity object
     */
    public CompleteInfoExportCollectionAdvanceModificationRequest toModel
            (CompleteInfoExportCollectionAdvanceModificationRequestDto completeInfoRequestDto) {
        if(completeInfoRequestDto == null) {
            return null;
        }
        CompleteInfoExportCollectionAdvanceModificationRequest completeInfoRequest =
                new CompleteInfoExportCollectionAdvanceModificationRequest();
        completeInfoRequest.setRequest(serializer.toModel(completeInfoRequestDto.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toModel(completeInfoRequestDto, completeInfoRequest);
        return completeInfoRequest;
    }
}
