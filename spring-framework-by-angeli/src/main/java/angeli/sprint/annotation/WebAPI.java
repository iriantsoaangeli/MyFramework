package angeli.sprint.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marque une methode de controller comme API : son retour est ecrit
 * directement dans la reponse (JSON par defaut) au lieu d'afficher une vue.
 * Ignoree si la methode retourne un {@link angeli.sprint.model.ModelAndView}.
 */
@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface WebAPI {
    /**
     * Content-Type de la reponse : "application/json" ou "text/html".
     * Toute autre valeur retombe sur du JSON.
     *
     * @return le Content-Type de la reponse
     */
    String contentType() default "application/json";
}
