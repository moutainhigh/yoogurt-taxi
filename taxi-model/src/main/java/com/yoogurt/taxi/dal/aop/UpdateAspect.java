package com.yoogurt.taxi.dal.aop;

import com.yoogurt.taxi.common.helper.ServletHelper;
import com.yoogurt.taxi.common.helper.TokenHelper;
import com.yoogurt.taxi.dal.annotation.Domain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Aspect
@Component
public class UpdateAspect {

    @Autowired
    private TokenHelper tokenHelper;

    @Before("execution(* com.yoogurt.taxi.dal.mapper..*..*update*(..))" +
        "||execution(* com.yoogurt.taxi.dal.mapper..*..*edit*(..))" +
        "||execution(* com.yoogurt.taxi.dal.mapper..*..*delete*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if(args != null && args.length > 0)
            Arrays.stream(args).forEach(arg -> {
                if (arg != null) {
                    if (arg instanceof List) {
                        List argList = (List) arg;
                        if (!CollectionUtils.isEmpty(argList)) {
                            argList.stream().forEach(this::domainHandle);
                        }
                    } else {
                        domainHandle(arg);
                    }
                }
            });
    }

    private void domainHandle(Object object) {
        if (object.getClass().isAnnotationPresent(Domain.class)) {
            Long userId = null;
            try {
                //不需要考虑token过期的情况了
                userId = tokenHelper.getUserId(ServletHelper.getRequest());
            } catch (Exception e) {
                log.error("获取用户ID失败,{}", e);
            }
            try {
                MethodUtils.invokeMethod(object, "setGmtModify", new Date());
                MethodUtils.invokeMethod(object, "setModifier", userId == null ? 0L : userId);
            } catch (Exception e) {
                log.error("设置公共字段失败,{}", e);
            }
        }
    }
}
