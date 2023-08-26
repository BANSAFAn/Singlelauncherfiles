package com.google.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface JsonAdapter {
  Class<?> value();
  
  boolean nullSafe() default true;
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\annotations\JsonAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */