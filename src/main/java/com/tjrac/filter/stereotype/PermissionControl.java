package com.tjrac.filter.stereotype;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionControl {
    String value() default "";   // 根据权限name查找
    int id() default -1;         // 根据权限id查找

}
