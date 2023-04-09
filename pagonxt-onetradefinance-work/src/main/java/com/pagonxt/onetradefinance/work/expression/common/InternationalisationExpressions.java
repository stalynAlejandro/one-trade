package com.pagonxt.onetradefinance.work.expression.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.platform.service.task.PlatformTaskService;
import com.flowable.platform.service.task.TaskRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * service class for internationalisation expressions
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.platform.service.task.PlatformTaskService
 * @since jdk-11.0.13
 */
@Service
public class InternationalisationExpressions {

    //Class attribute
    private final PlatformTaskService platformTaskService;

    /**
     * Constructor method
     * @param platformTaskService : a PlatformTaskService object
     */
    public InternationalisationExpressions(PlatformTaskService platformTaskService) {
        this.platformTaskService = platformTaskService;
    }

    /**
     * Method to get the task name
     * @param taskId        : a string with the task id
     * @param languageCode  : a string with the language code
     * @return a string with the task name
     */
    public String getTaskName(String taskId, String languageCode){
        TaskRepresentation task = platformTaskService.getTask(taskId, true);
        ObjectNode translationsNode = task.getTranslations();
        if (translationsNode == null || translationsNode.get(languageCode) == null || translationsNode.get(languageCode).get("name") == null) {
            return StringUtils.substringBefore(StringUtils.defaultString(task.getName(),""),"|").trim();
        } else {
            return StringUtils.substringBefore(translationsNode.get(languageCode).get("name").asText(),"|").trim();
        }
    }
}
