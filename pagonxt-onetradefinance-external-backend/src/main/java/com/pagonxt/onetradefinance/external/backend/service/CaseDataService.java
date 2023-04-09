package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class CaseDataService {

    /**
     * Class variables
     * ObjectMapper provides functionality for reading and writing JSON,
     * either to and from basic POJOs (Plain Old Java Objects),
     * or to and from a general-purpose JSON Tree Model
     */
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public CaseDataService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * class method
     * @param requestId the request id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a list of comments of the case
     */
    public List<Comment> getComments(String requestId, UserInfo userInfo) {
        ControllerResponse response =
                restTemplateWorkService.findComments(requestId, new AuthenticatedRequest(userInfo));
        return mapComments(response);
    }

    /**
     * Method to map comments
     * @param response ControllerResponse object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return a list of comments, mapped
     */
    private List<Comment> mapComments(ControllerResponse response) {
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Comment.class);
        return mapper.convertValue(response.getEntity(), type);
    }
}
