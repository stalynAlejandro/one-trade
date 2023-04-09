package com.pagonxt.onetradefinance.work.common;

import java.util.Arrays;
import java.util.List;

/**
 * Class with constants of extension elements, to set the category of a task
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ExtensionElementsConstants {

    private ExtensionElementsConstants() {}

    public static final List<String> CATEGORY_LEVEL_EXTENSION_ELEMENT_KEYS =
            Arrays.asList("categoryL1", "categoryL2", "categoryL3", "isExternalTask", "externalTaskType");

    public static final String USER_TASK_LABELS_EXTENSION_ELEMENT_KEY = "userTaskLabels";

    public static final String USER_TASK_LABELS_EXTENSION_ELEMENT_NAME = "label";
    public static final String USER_TASK_LABELS_EXTENSION_ELEMENT_VARIABLE_NAME = "tags";
}
