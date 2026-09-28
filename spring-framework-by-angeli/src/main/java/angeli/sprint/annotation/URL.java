package angeli.sprint.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * URL
 * lien et methode HTTP pour les controllers
 */
@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface URL {
    String value() default "";
    String method() default "GET";
}
