package com.kirayous.common.annotation;

import java.lang.annotation.*;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.annotation
 * @date 2021/9/18 10:10
 */
@Target(ElementType.METHOD) //作用于方法上
@Retention(RetentionPolicy.RUNTIME) //运行时
@Documented//生成文档
public @interface OptLog {
    /**
     * @return 操作类型
     */
    String optType() default "";
}
