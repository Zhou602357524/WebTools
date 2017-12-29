package com.xwtec.tools.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;


@Configuration
@Aspect
public class MethodTimeAdvice{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.xwtec.tools.core..*.*(..))")
    private void serviceMethod() {}

    @Around("serviceMethod()")
    public Object logMethodRunningTime(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        StopWatch watch = new StopWatch();
        watch.start();
        Object retVal = pjp.proceed();
        // stop stopwatch
        watch.stop();
        double time = watch.getTotalTimeSeconds();
        logger.info("方法{}执行了,总共耗时:{}",pjp.getSignature().getName(),time);
        return retVal;
    }

}
