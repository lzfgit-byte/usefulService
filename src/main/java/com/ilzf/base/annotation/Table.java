package com.ilzf.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface Table {
    String value() default "";
}
