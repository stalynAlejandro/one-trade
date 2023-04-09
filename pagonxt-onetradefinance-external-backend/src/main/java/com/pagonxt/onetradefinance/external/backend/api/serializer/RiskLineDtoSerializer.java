package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
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
public class RiskLineDtoSerializer {

    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties To get the timezone a dateformat for serialize and deserialize data
     */
    public RiskLineDtoSerializer(DateFormatProperties dateFormatProperties) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * This method converts data from a DTO object to an entity object
     * @param riskLineDto a DTO object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto
     * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
     * @return an entity object
     */
    public RiskLine toModel(RiskLineDto riskLineDto) {
        if (riskLineDto == null) {
            return new RiskLine();
        }
        RiskLine riskLine = new RiskLine();
        riskLine.setRiskLineId(riskLineDto.getRiskLineId());
        riskLine.setClient(riskLineDto.getClient());
        riskLine.setIban(riskLineDto.getIban());
        riskLine.setStatus(riskLineDto.getStatus());
        riskLine.setLimitAmount(riskLineDto.getLimitAmount());
        riskLine.setAvailableAmount(riskLineDto.getAvailableAmount());
        if (Strings.isNotBlank(riskLineDto.getExpires())) {
            try {
                riskLine.setExpires(dateTimeFormat.parse(riskLineDto.getExpires()));
            } catch (ParseException e) {
                throw new DateFormatException(riskLineDto.getExpires(), e);
            }
        }
        riskLine.setCurrency(riskLineDto.getCurrency());
        return riskLine;
    }

    /**
     * This method converts data from an entity object to a DTO object
     * @param riskLine an entity object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto
     * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
     * @return a DTO object
     */
    public RiskLineDto toDto(RiskLine riskLine) {
        if(riskLine == null) {
            return null;
        }
        RiskLineDto riskLineDto = new RiskLineDto();
        riskLineDto.setRiskLineId(riskLine.getRiskLineId());
        riskLineDto.setClient(riskLine.getClient());
        riskLineDto.setIban(riskLine.getIban());
        riskLineDto.setStatus(riskLine.getStatus());
        riskLineDto.setLimitAmount(riskLine.getLimitAmount());
        riskLineDto.setAvailableAmount(riskLine.getAvailableAmount());
        if (riskLine.getExpires() != null) {
            riskLineDto.setExpires(dateTimeFormat.format(riskLine.getExpires()));
        }
        riskLineDto.setCurrency(riskLine.getCurrency());
        return riskLineDto;
    }
}
