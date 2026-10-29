package angeli.sprint.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Associe une methode de controller a une URL et a une methode HTTP.
 * Un couple URL + methode HTTP ne peut etre utilise qu'une seule fois.
 */
@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface URL {
    /**
     * Chemin relatif au contexte de l'application, exemple : "/eleve/liste".
     *
     * @return le chemin de la route
     */
    String value() default "";

    /**
     * Methode HTTP acceptee : "GET" ou "POST".
     *
     * @return la methode HTTP
     */
    String method() default "GET";
}
