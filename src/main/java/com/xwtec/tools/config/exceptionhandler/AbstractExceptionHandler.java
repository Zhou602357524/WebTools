package com.xwtec.tools.config.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void printException(Throwable e){
        e.printStackTrace();
    }

}
