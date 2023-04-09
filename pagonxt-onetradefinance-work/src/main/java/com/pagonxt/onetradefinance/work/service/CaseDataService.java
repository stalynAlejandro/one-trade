package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.flowable.platform.service.task.PlatformTaskService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.IS_EXTERNAL_TASK;
import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.OPERATION_COMMENTS;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.*;

/**
 * service class for case data
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @see com.pagonxt.onetradefinance.work.security.CaseSecurityService
 * @see org.flowable.cmmn.api.CmmnHistoryService
 * @since jdk-11.0.13
 */
@Service
public class CaseDataService {

    public static final String SEPARATOR = "|";
    public static final String BACKOFFICE_NAME = "Back Office";
    public static final String KEY_FIND_COMMENTS = "findComments";

    //class attributes
    private final ObjectMapper mapper;
    private final PlatformTaskService platformTaskService;
    private final CaseSecurityService caseSecurityService;
    private final CmmnHistoryService cmmnHistoryService;

    /**
     * constructor method
     * @param mapper                : a ObjectMapper object
     * @param platformTaskService   : a PlatformTaskService object
     * @param caseSecurityService   : a CaseSecurityService object
     * @param cmmnHistoryService    : a CmmnHistoryService object
     */
    public CaseDataService(ObjectMapper mapper,
                           PlatformTaskService platformTaskService,
                           CaseSecurityService caseSecurityService,
                           CmmnHistoryService cmmnHistoryService) {
        this.mapper = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.platformTaskService = platformTaskService;
        this.caseSecurityService = caseSecurityService;
        this.cmmnHistoryService = cmmnHistoryService;
    }

    /**
     * Method to find comments
     * @param request  : an AuthenticatedRequest object
     * @param requestId: a string with the request id
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.Comment
     * @return a list with comments
     */
    public List<Comment> findComments(AuthenticatedRequest request, String requestId) {
        Map<String, Object> caseVariables = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .includeCaseVariables()
                .variableValueEquals(requestId)
                .singleResult()
                .getCaseVariables();
        if(caseVariables == null) {
            throw new ResourceNotFoundException(String
                    .format("No case found with id %s", requestId), KEY_FIND_COMMENTS);
        }
        caseSecurityService.checkRead(request.getRequester(), caseVariables);
        List<Comment> comments = getComments(caseVariables);
        // Filter by user type
        String userType = request.getRequester().getUser().getUserType();
        comments = filterComments(comments, userType);
        String locale = request.getRequester().getLocale();
        adaptComments(comments, locale);
        // Reverse chronological order
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getTimestamp).reversed()).collect(Collectors.toList());
    }

    /**
     * Method to get comments
     * @param caseVariables: a collection of cases variables
     * @see com.pagonxt.onetradefinance.integrations.model.Comment
     * @return a list with comments
     */
    private List<Comment> getComments(Map<String, Object> caseVariables) {
        ArrayNode commentsArrayNode = (ArrayNode) caseVariables.get(OPERATION_COMMENTS);
        try {
            return mapper.readValue(commentsArrayNode.toString(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new ServiceException("Processing error retrieving comments", KEY_FIND_COMMENTS, e);
        }
    }

    /**
     * Method to filter comments
     * @param comments: a list with comments
     * @param userType: a string with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.Comment
     * @return a list with comments
     */
    private List<Comment> filterComments(List<Comment> comments, String userType) {
        Predicate<Comment> visibilityFilter;
        switch (userType) {
            case USER_TYPE_CUSTOMER:
                visibilityFilter = Comment::getVisibleForClient;
                break;
            case USER_TYPE_OFFICE:
            case USER_TYPE_MIDDLE_OFFICE:
                visibilityFilter = Comment::getVisibleForOffice;
                break;
            case USER_TYPE_BACKOFFICE: {
                visibilityFilter = c -> true;
            }
            break;
            default:
                visibilityFilter = c -> false;
        }
        return comments.stream().filter(visibilityFilter).collect(Collectors.toList());
    }

    /**
     * Method to filter comments
     * @param comments: a list with comments
     * @param locale  : a string with locale value
     * @see com.pagonxt.onetradefinance.integrations.model.Comment
     */
    private void adaptComments(List<Comment> comments, String locale) {
        for (Comment comment : comments) {
            if (comment.getTaskId() == null) {
                comment.setTaskName(getDefaultTaskName(locale));
            } else {
                // Find translated task name
                String taskName;
                TaskRepresentation task = platformTaskService.getTask(comment.getTaskId(), true);
                JsonNode translation = task.getTranslations().get(locale);
                if (translation != null && translation.get("name") != null) {
                    taskName = translation.get("name").asText();
                } else {
                    taskName = task.getName();
                }
                if (StringUtils.contains(taskName, SEPARATOR)) {
                    taskName = StringUtils.split(taskName, SEPARATOR)[0].trim();
                }
                comment.setTaskName(taskName);
                // Set username from backoffice
                Map<String, Object> taskVariables = platformTaskService.getTaskVariables(comment.getTaskId());
                if (taskVariables == null) {
                    throw new ResourceNotFoundException(String
                            .format("No task found with id %s", comment.getTaskId()), KEY_FIND_COMMENTS);
                }
                Map<String, Object> commentTask = (Map<String, Object>) taskVariables.get("task");
                if (commentTask != null && !Boolean.TRUE.toString().equals(commentTask.get(IS_EXTERNAL_TASK))) {
                    comment.setUserDisplayedName(BACKOFFICE_NAME);
                }
            }
        }
    }

    /**
     * Method to get the default task name
     * @param locale  : a string with locale value
     * @return a string object with the default task name
     */
    private String getDefaultTaskName(String locale) {
        switch(locale) {
            case "es_es" :
                return "Alta";
            case "en_us":
            default:
                return "Request";
        }
    }

}
