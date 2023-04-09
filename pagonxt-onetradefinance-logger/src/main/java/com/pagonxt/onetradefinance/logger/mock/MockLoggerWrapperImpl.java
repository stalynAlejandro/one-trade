package com.pagonxt.onetradefinance.logger.mock;

import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MockLoggerWrapperImpl implements LoggerWrapper {

    private static final Logger log = LoggerFactory.getLogger(MockLoggerWrapperImpl.class);
    public static final String SEPARATOR = " - ";

    @Override
    public void businessLog(String level, Object... objs) {
        StringBuilder message = new StringBuilder();
        if (objs.length > 0) {
            Arrays.stream(objs).sequential().forEach(obj -> constructMessage(message, obj));
            String msg = message.substring(0, message.length() - SEPARATOR.length());
            logByLevel(level, msg);
        }
    }

    private void constructMessage(StringBuilder message, Object obj) {
        String msg;
        if (obj instanceof String) {
            msg = (String) obj;
        } else if (obj != null) {
            msg = obj.toString();
        } else {
            msg = "null";
        }
        message.append(msg);
        message.append(SEPARATOR);
    }

    private void logByLevel(String level, String message) {
        switch (level) {
            case "trace":
                if (log.isTraceEnabled()) {
                    log.trace(message);
                }
                break;
            case "error":
                if (log.isErrorEnabled()) {
                    log.error(message);
                }
                break;
            case "info":
                if (log.isInfoEnabled()) {
                    log.info(message);
                }
                break;
            default:
                if (log.isDebugEnabled()) {
                    log.debug(message);
                }
                break;
        }
    }
}
