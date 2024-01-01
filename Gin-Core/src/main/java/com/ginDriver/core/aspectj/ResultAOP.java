//package com.ginDriver.core.aspectj;
//
//import com.ginDriver.core.domain.vo.ResultVO;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * @author DueGin
// */
//@Slf4j
//@Aspect
//@Component
//public class ResultAOP {
//
//    @AfterReturning(value = "execution(* com.ginDriver.*.controller.*Controller(..))", returning = "methodResult")
//    public ResultVO after(JoinPoint jp, Object methodResult) {
//        if (methodResult != null) {
//            ResultVO vo;
//            if (methodResult instanceof ResultVO) {
//                vo = (ResultVO) methodResult;
//            } else {
//                vo = ResultVO.ok(methodResult);
//            }
//            return vo;
//        }
//        return null;
//    }
//}
