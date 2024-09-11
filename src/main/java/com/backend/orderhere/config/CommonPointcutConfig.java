package com.backend.orderhere.config;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcutConfig {

    @Pointcut("execution(* com.backend.orderhere.filter.*.*(..))")
    public void filterPackageConfig() {}

    @Pointcut("execution(* com.backend.orderhere.auth.*.*(..))")
    public void authPackageConfig() {}

    @Pointcut("execution(* com.backend.orderhere.service.*.*(..))")
    public void serviceBeans() {}

    @Pointcut("@annotation(com.backend.orderhere.annotations.TrackTime)")
    public void trackTimeAnnotation() {}
}
