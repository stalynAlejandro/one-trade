package com.pagonxt.onetradefinance.logger.impl;

import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import com.santander.gtscore.observability.log.UtilsLog;

public class LoggerWrapperImpl implements LoggerWrapper {

    @Override
    public void businessLog(String level, Object... obs) {
        UtilsLog.businessLog(level, obs);
    }
}
