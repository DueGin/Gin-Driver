package com.ginDriver.core.log.aspectj;

import com.ginDriver.core.log.GinLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author DueGin
 */
@Slf4j
@Aspect
@Component
public class GinLogHandler {

    @Value("${ginLog.prefix:==>}")
    private String logPrefix;

    @Value("${ginLog.suffix:}")
    private String logSuffix;


    @Around("@annotation(ginLog)")
    public Object around(ProceedingJoinPoint pjp, GinLog ginLog) {
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodName = signature.getName();
        String packageMethodName = pjp.getTarget().getClass().getName() + "#" + methodName;
        log.info("{} 执行{}, 入参: {} {}", logPrefix, packageMethodName, Arrays.toString(args), logSuffix);

        Object ret = null;
        long startMills = 0, endMills = 0;
        try {
            if (ginLog.isTiming()) {
                startMills = System.currentTimeMillis();
            }
            // 执行方法本体
            ret = pjp.proceed(args);
            if (ginLog.isTiming()) {
                endMills = System.currentTimeMillis();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Class<?> returnType = signature.getReturnType();

        String endLog = logPrefix + " 执行结束" + packageMethodName;
        if (ginLog.isTiming()) {
            endLog += ", 耗时: " + (endMills - startMills) + "毫秒";
        }
        if (!returnType.equals(Void.TYPE)) {
            endLog += ", 返回值: " + ret;
        }

        log.info(endLog + " " + logSuffix);
        return ret;
    }
}
