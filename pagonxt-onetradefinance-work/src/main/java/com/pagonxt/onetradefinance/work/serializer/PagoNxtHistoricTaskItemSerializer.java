package com.pagonxt.onetradefinance.work.serializer;

import com.pagonxt.onetradefinance.integrations.model.historictask.PagoNxtHistoricTaskItem;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.Translation;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.IS_EXTERNAL_TASK;
import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.TASK_COMPLETION_USER_DISPLAYED_NAME;

/**
 * Service class to serialize data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Service
public class PagoNxtHistoricTaskItemSerializer {
    private static final String SEPARATOR = "|";

    /**
     * Method to serialize some fields
     * @param instance  : a Task object
     * @param locale    : a string with the locale value
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.PagoNxtHistoricTaskItem
     * @return a PagoNxtHistoricTaskItem object
     */
    public PagoNxtHistoricTaskItem serialize(Task instance, String locale) {
        if (instance == null) {
            return null;
        }
        PagoNxtHistoricTaskItem result = new PagoNxtHistoricTaskItem();
        result.setRowId(getUUID());
        result.setStartDate(instance.getCreateTime());
        result.setEndDate(instance.getEndTime());
        result.setTaskName(getFormattedTaskName(instance, locale));
        result.setUserName(getUserName(instance));
        return result;
    }

    /**
     * Method to get the UUID
     * @return a string object with the UUID
     */
    protected String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Method to get the task name, formatted
     * @param instance  : a Task object
     * @param locale    : a string with the locale value
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return a string with the task name, formatted
     */
    private String getFormattedTaskName(Task instance, String locale) {
        String taskName = getTaskName(instance, locale);
        if (StringUtils.contains(taskName, SEPARATOR)) {
            return StringUtils.split(taskName, SEPARATOR)[0].trim();
        }
        return taskName;
    }

    /**
     * Method to get the task name
     * @param instance  : a Task object
     * @param locale    : a string with the locale value
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return a string with the task name
     */
    private String getTaskName(Task instance, String locale) {
        Translation translation = instance.getTranslations().stream()
                .filter(t -> t.getLocale().equals(locale) && "name".equals(t.getKey()))
                .findFirst().orElse(null);
        if(translation == null) {
            return instance.getName();
        }
        return translation.getValue();
    }

    /**
     * Method to get the username
     * @param instance  : a Task object
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @return a string with the username
     */
    private String getUserName(Task instance) {
        String isExternalTask = TaskUtils.getString(instance, IS_EXTERNAL_TASK);
        if (Boolean.TRUE.toString().equals(isExternalTask)) {
            String username = TaskUtils.getString(instance, TASK_COMPLETION_USER_DISPLAYED_NAME);
            if(username != null) {
                return username;
            }
        }
        return "Back Office";
    }
}
