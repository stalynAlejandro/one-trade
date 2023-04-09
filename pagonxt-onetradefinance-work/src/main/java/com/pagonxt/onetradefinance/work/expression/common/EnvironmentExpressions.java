package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.platform.service.task.PlatformTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * service class for environment expressions
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @since jdk-11.0.13
 */
@Component
public class EnvironmentExpressions {
    private static final String HYPHEN = "-";
    private static final String UNDERSCORE = "_";
    public static final String URL_FORMAT = "%s/app/%s/%s/%s/%s";
    public static final String URL_FORMAT_REQUESTS_QUERY = "%s/app/%s";
    public static final String REQUESTS_QUERY = "requests-query";

    /**
     * class attributes
     */
    private final String externalAppBaseUrl;
    private final PlatformTaskService platformTaskService;

    /**
     * constructor method
     * @param platformTaskService : a PlatformTaskService object
     * @param externalAppBaseUrl  : a string with the base URL of the external app
     */
    public EnvironmentExpressions(PlatformTaskService platformTaskService,
                                  @Value("${flowable.frontend.endpoints.external-app}") String externalAppBaseUrl) {
        this.platformTaskService = platformTaskService;
        this.externalAppBaseUrl = externalAppBaseUrl;
    }

    /**
     * This method allows to obtain the URL of an external task
     * @param taskId : a string with the task id
     * @return a string with the URL
     */
    public String getExternalTaskUrl(String taskId) {
        Map<String, Object> variables = platformTaskService.getTaskVariables(taskId);
        if(variables == null) {
            return null;
        }
        Map<String, Object> root = (Map<String, Object>) variables.get(ROOT);
        if(root == null) {
            return null;
        }
        String product = ((String) root.get(PRODUCT)).replace(UNDERSCORE, HYPHEN).toLowerCase();
        String event = ((String) root.get(EVENT)).replace(UNDERSCORE, HYPHEN).toLowerCase();
        String taskType = ((String) variables.get(EXTERNAL_TASK_TYPE)).replace(UNDERSCORE, HYPHEN).toLowerCase();
        return String.format(URL_FORMAT, externalAppBaseUrl, product, event, taskType, taskId);
    }

    /**
     * This method allows to obtain the URL of an external request query
     * @return a string with the URL
     */
    public String getExternalRequestsQueryUrl() {
        return String.format(URL_FORMAT_REQUESTS_QUERY, externalAppBaseUrl, REQUESTS_QUERY);
    }
}
