package cn.xiangstudy.config.annotation;

import java.lang.annotation.*;

/**
 * @author zhangxiang
 * @date 2024-10-31 9:35
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//@Documented
public @interface AopTest {
    String tableAlias() default "t";
}
