package com.pagonxt.onetradefinance.external.backend.utils;

import com.pagonxt.onetradefinance.external.backend.api.model.Filters;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FilterUtils {

    private FilterUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getString(Filters filters, String filter) {
        if (!filters.containsKey(filter)) {
            return null;
        }
        String value = filters.get(filter).getString();
        return StringUtils.hasText(value) ? value : null;
    }

    public static List<String> getStringList(Filters filters, String filter) {
        if (!filters.containsKey(filter)) {
            return Collections.emptyList();
        }
        List<String> value = filters.get(filter).getStringList();
        return (value == null || value.isEmpty()) ? Collections.emptyList() : value;
    }

    public static Date getJSONDate(Filters filters, String filter) {
        if (!filters.containsKey(filter)) {
            return null;
        }
        return filters.get(filter).getJSONDate();
    }

    public static Double getDouble(Filters filters, String filter) {
        if (!filters.containsKey(filter)) {
            return null;
        }
        return filters.get(filter).getDouble();
    }
}
