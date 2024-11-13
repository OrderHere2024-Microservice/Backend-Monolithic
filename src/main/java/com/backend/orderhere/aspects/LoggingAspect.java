package com.backend.orderhere.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // @Before("com.backend.orderhere.config.CommonPointcutConfig.serviceBeans()")
    public void logMethodCallBeforeExecution(JoinPoint joinPoint) {
        logger.info("Before Aspect: {} - is called with arguments - {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    // @After("com.backend.orderhere.config.CommonPointcutConfig.serviceBeans()")
    public void logMethodCallAfterExecution(JoinPoint joinPoint) {
        logger.info("After Aspect: {} has executed", joinPoint.getSignature());
    }

    @AfterThrowing(pointcut = "com.backend.orderhere.config.CommonPointcutConfig.authPackageConfig()", throwing = "exception")
    public void logMethodCallAfterException(JoinPoint joinPoint, Exception exception) {
        logger.error("AfterThrowing Aspect: {} has thrown an exception: {}", joinPoint.getSignature(), exception.getMessage());
    }

    @AfterReturning(pointcut = "com.backend.orderhere.config.CommonPointcutConfig.filterPackageConfig()", returning = "resultValue")
    public void logMethodCallAfterSuccessfulExecution(JoinPoint joinPoint, Object resultValue) {
        logger.info("AfterReturning Aspect: {} has returned: {}", joinPoint.getSignature(), resultValue);
    }

}
