package com.pagonxt.onetradefinance.work.utils;

import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.service.model.Variable;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Class with case variable utils
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CaseVariableUtils {

    private static final List<String> INTEGER_NUMERIC_TYPES = List.of("integer", "long");
    private static final List<String> DECIMAL_NUMERIC_TYPES = List.of("float", "double");

    /**
     * empty constructor method
     */
    private CaseVariableUtils() {
    }

    /**
     * class method
     * @param instance : a Case object with the instance
     * @param name     : a string object with the name
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a string object
     */
    public static String getString(Case instance, String name) {
        return instance.getVariables().parallelStream()
                .filter(variable -> name.equals(variable.getName()) &&
                        "string".equals(variable.getType()) && variable.getTextValue() != null)
                .map(Variable::getTextValue)
                .findFirst().orElse(null);
    }

    /**
     * class method
     * @param instance      : a Case object with the instance
     * @param name          : a string object with the name
     * @param defaultValue  : a string object with the default value
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a string object
     */
    public static String getStringOrDefault(Case instance, String name, String defaultValue) {
        return instance.getVariables().parallelStream()
                .filter(variable -> name.equals(variable.getName()) &&
                        "string".equals(variable.getType()) && variable.getTextValue() != null)
                .map(Variable::getTextValue)
                .findFirst().orElse(defaultValue);
    }

    /**
     * class method
     * @param instance : a Case object with the instance
     * @param name     : a string object with the name
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a Double object
     */
    public static Double getDouble(Case instance, String name) {
        return instance.getVariables().parallelStream()
                .filter(variable -> name.equals(variable.getName()) &&
                        DECIMAL_NUMERIC_TYPES.contains(variable.getType()) &&
                        StringUtils.hasText(variable.getRawValue()))
                .map(variable -> Double.parseDouble(variable.getRawValue()))
                .findFirst()
                .orElse(null);
    }

    /**
     * class method
     * @param instance : a Case object with the instance
     * @param name     : a string object with the name
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return an Integer object
     */
    public static Integer getInteger(Case instance, String name) {
        return instance.getVariables().parallelStream()
                .filter(variable -> name.equals(variable.getName()) &&
                        INTEGER_NUMERIC_TYPES.contains(variable.getType()) &&
                        StringUtils.hasText(variable.getRawValue()))
                .map(variable -> Integer.parseInt(variable.getRawValue()))
                .findFirst().orElse(null);
    }

    /**
     * class method
     * @param instance : a Case object with the instance
     * @param name     : a string object with the name
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @return a Date object
     */
    public static Date getDate(Case instance, String name) {
        return instance.getVariables().parallelStream()
                .filter(variable -> name.equals(variable.getName()) &&
                        "date".equals(variable.getType()) && StringUtils.hasText(variable.getRawValue()))
                .map(Variable::getDateValue)
                .findFirst().orElse(null);
    }
}
