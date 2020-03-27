package org.yuan.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* org.yuan.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("====== 开始执行 {}.{}======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if(takeTime > 3000){
            logger.error("====== 结束执行， 耗时：{} 毫秒 ======",takeTime);
        }else if (takeTime > 2000){
            logger.warn("====== 结束执行， 耗时：{} 毫秒 ======",takeTime);
        }else{
            logger.info("====== 结束执行， 耗时：{} 毫秒 ======",takeTime);
        }

        return result;
    }
}
