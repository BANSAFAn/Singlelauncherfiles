package ch.qos.logback.core.joran.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DefaultClass {
  Class<?> value();
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\joran\spi\DefaultClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */