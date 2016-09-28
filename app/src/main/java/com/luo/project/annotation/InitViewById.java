package com.luo.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * InitViewById
 * <p/>
 * Created by luoyingxing on 16/9/28.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InitViewById {
    //id就表示哪些控件，-1就表示取不到时候的默认值
    int id() default -1;
}