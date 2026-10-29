package angeli.sprint.annotation ;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation pour dire que cet url a besoin d'un attribut de session present 
 */
@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Auth {
    String[] roles() default {};
}