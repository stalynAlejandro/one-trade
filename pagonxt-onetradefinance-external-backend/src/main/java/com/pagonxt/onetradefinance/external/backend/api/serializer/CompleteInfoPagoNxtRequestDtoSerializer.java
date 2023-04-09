package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.CompleteInfoPagoNxtRequestDto;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoPagoNxtRequestDtoSerializer {

    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties To get the timezone a dateformat for serialize and deserialize data
     */
    public CompleteInfoPagoNxtRequestDtoSerializer(DateFormatProperties dateFormatProperties) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Converts data from a completeInfoRequest to a completeInfoRequestDto (complete info task)
     * @param completeInfoRequest an entity object
     * @param completeInfoRequestDto a DTO object
     * @param <T1> generic class
     * @param <T2> generic class
     * @return null if any received parameter has no data or a DTO object
     */
    public <T1 extends CompleteInfoPagoNxtRequestDto, T2 extends CompleteInfoPagoNxtRequest> T1 toDto
    (T2 completeInfoRequest, T1 completeInfoRequestDto) {
        if (completeInfoRequestDto == null) {
            return null;
        }
        if (completeInfoRequest == null) {
            return completeInfoRequestDto;
        }
        completeInfoRequestDto.setReturnReason(completeInfoRequest.getReturnReason());
        completeInfoRequestDto.setReturnComment(completeInfoRequest.getReturnComment());
        if (completeInfoRequest.getRequestCreationTime() != null) {
            completeInfoRequestDto.setRequestCreationTime(dateTimeFormat.
                    format(completeInfoRequest.getRequestCreationTime()));
        }
        completeInfoRequestDto.setRequestCreatorName(completeInfoRequest.getRequestCreatorName());
        if (completeInfoRequest.getTaskCreationTime() != null) {
            completeInfoRequestDto.setTaskCreationTime(dateTimeFormat.
                    format(completeInfoRequest.getTaskCreationTime()));
        }
        return completeInfoRequestDto;
    }

    /**
     * Converts data from a completeInfoRequestDto to a completeInfoRequest (complete info task)
     * @param completeInfoRequestDto a DTO object
     * @param completeInfoRequest an entity object
     * @param <T1> generic class
     * @param <T2> generic class
     * @return null if any received parameter has no data or an entity object
     */
    public <T1 extends CompleteInfoPagoNxtRequestDto, T2 extends CompleteInfoPagoNxtRequest> T2 toModel
            (T1  completeInfoRequestDto, T2 completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        if(completeInfoRequestDto == null) {
            return completeInfoRequest;
        }
        completeInfoRequest.setReturnReason(completeInfoRequestDto.getReturnReason());
        completeInfoRequest.setReturnComment(completeInfoRequestDto.getReturnComment());
        if(Strings.isNotBlank(completeInfoRequestDto.getRequestCreationTime())) {
            try {
                completeInfoRequest.setRequestCreationTime(dateTimeFormat.
                        parse(completeInfoRequestDto.getRequestCreationTime()));
            } catch (ParseException e) {
                throw new DateFormatException(completeInfoRequestDto.getRequestCreationTime(), e);
            }
        }
        completeInfoRequest.setRequestCreatorName(completeInfoRequestDto.getRequestCreatorName());
        if(Strings.isNotBlank(completeInfoRequestDto.getTaskCreationTime())) {
            try {
                completeInfoRequest.setTaskCreationTime(dateTimeFormat.
                        parse(completeInfoRequestDto.getTaskCreationTime()));
            } catch (ParseException e) {
                throw new DateFormatException(completeInfoRequestDto.getTaskCreationTime(), e);
            }
        }
        return completeInfoRequest;
    }
}
