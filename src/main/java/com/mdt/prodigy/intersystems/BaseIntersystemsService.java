package com.mdt.prodigy.intersystems;

import com.intersystems.enslib.pex.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all Intersystems productions
 */
@Slf4j
public abstract class BaseIntersystemsService extends BusinessService {

    // These are variables set within intersytsems.
    public String environment = "app";        // What environment the system is running in.

    boolean isRunningAsJavaApp(){
        return "app".equalsIgnoreCase(environment);
    }

    public void LOGERROR(String message) {
        if (isRunningAsJavaApp()) {
            log.error(message);
        } else {
            super.LOGERROR(message);
        }
    }

    public void LOGINFO(String message) {
        if (isRunningAsJavaApp()) {
            log.info(message);
        } else {
            super.LOGINFO(message);
        }
    }

    public void LOGWARNING(String message) {
        if (isRunningAsJavaApp()) {
            log.warn(message);
        } else {
            super.LOGWARNING(message);
        }
    }

}

