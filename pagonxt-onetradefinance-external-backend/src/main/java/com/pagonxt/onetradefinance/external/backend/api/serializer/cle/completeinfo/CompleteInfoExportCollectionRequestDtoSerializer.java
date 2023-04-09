package com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo.CompleteInfoExportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoPagoNxtRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionRequestDtoSerializer
 * @see CompleteInfoPagoNxtRequestDtoSerializer
 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoExportCollectionRequestDtoSerializer {

    /**
     * class attributes
     */
    private final ExportCollectionRequestDtoSerializer serializer;
    private final CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer;

    /**
     * Class Constructor
     * @param serializer ExportCollectionRequestDtoSerializer object
     * @param completeInfoPagoNxtRequestDtoSerializer CompleteInfoPagoNxtRequestDtoSerializer object
     */
    public CompleteInfoExportCollectionRequestDtoSerializer
            (ExportCollectionRequestDtoSerializer serializer,
             CompleteInfoPagoNxtRequestDtoSerializer completeInfoPagoNxtRequestDtoSerializer) {
        this.serializer = serializer;
        this.completeInfoPagoNxtRequestDtoSerializer = completeInfoPagoNxtRequestDtoSerializer;
    }

    /**
     * This method converts data from a entity class to a DTO class
     * @param completeInfoRequest a entity class
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest
     * @return null, if completeInfoRequest doesn't have data, or a DTO object
     */
    public CompleteInfoExportCollectionRequestDto toDto(CompleteInfoExportCollectionRequest completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        CompleteInfoExportCollectionRequestDto completeInfoRequestDto = new CompleteInfoExportCollectionRequestDto();
        completeInfoRequestDto.setRequest(serializer.toDto(completeInfoRequest.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toDto(completeInfoRequest, completeInfoRequestDto);
        return completeInfoRequestDto;
    }

    /**
     * This method converts data from A DTO class to an entity class
     * @param completeInfoRequestDto a DTO class
     * @see CompleteInfoExportCollectionRequestDto
     * @return null, if completeInfoRequestDto doesn't have data, or a entity object
     */
    public CompleteInfoExportCollectionRequest toModel(CompleteInfoExportCollectionRequestDto completeInfoRequestDto) {
        if(completeInfoRequestDto == null) {
            return null;
        }
        CompleteInfoExportCollectionRequest completeInfoRequest = new CompleteInfoExportCollectionRequest();
        completeInfoRequest.setRequest(serializer.toModel(completeInfoRequestDto.getRequest()));
        completeInfoPagoNxtRequestDtoSerializer.toModel(completeInfoRequestDto, completeInfoRequest);
        return completeInfoRequest;
    }
}
