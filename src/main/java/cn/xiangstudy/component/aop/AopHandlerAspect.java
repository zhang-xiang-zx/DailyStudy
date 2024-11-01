package cn.xiangstudy.component.aop;

import cn.xiangstudy.config.annotation.AopTest;
import cn.xiangstudy.pojo.BaseEntity;
import cn.xiangstudy.pojo.User;
import cn.xiangstudy.utils.InspectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxiang
 * @date 2024-10-31 9:41
 */
@Component
@Aspect
//@Order(1) // 执行顺序，数字越小顺序靠前
public class AopHandlerAspect {

    @Before("@annotation(aopTest)")
    public void before(JoinPoint joinPoint, AopTest aopTest) {
        System.out.println("拦截到了");
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof Map) {
            Map<String, Object> paramsMap = (Map<String, Object>) arg;
            paramsMap.put("dataScope", " AND (status = 0)");
        } else {
            try {
//                Class<?> aClass = arg.getClass();
                /*
                Field declaredField = aClass.getDeclaredField("status");
                Method getStatus = aClass.getMethod("getStatus");
                Method setStatus = aClass.getMethod("setStatus", declaredField.getType());
                setStatus.invoke(arg,0);
                 */
                /*
                Field declaredField = aClass.getDeclaredField("params");
                Method getParams = aClass.getMethod("getParams");
                Method setParams = aClass.getMethod("setParams", declaredField.getType());
                Map<String,Object> nq = new HashMap<>();
                nq.put("dataScope"," AND (status = 0)");
                setParams.invoke(arg,nq);
                 */
                /*
                BaseEntity baseEntity = (BaseEntity) arg;
                Map<String,Object> nq = new HashMap<>();
                nq.put("dataScope"," AND (status = 0)");
                baseEntity.setParams(nq);
                 */
                User user = (User) arg;
                System.out.println("未更改前：" + user.getParams());
                Map<String, Object> nq = new HashMap<>();
                nq.put("dataScope", " AND (status = 0)");
                user.setParams(nq);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void clearDataScope(JoinPoint joinPoint) {
        System.out.println("进到清理");
        Object params = joinPoint.getArgs()[0];
        if (InspectUtils.notNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put("dataScope", " AND status = 1");
            System.out.println("更改完毕");
        }
        BaseEntity baseEntity = (BaseEntity) params;
        System.out.println("清理内：" + baseEntity.getParams());
    }
}

