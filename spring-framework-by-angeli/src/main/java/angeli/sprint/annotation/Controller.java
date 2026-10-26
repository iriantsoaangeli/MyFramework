package angeli.sprint.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Marque une classe comme controller.
 * Les classes annotees sont trouvees au demarrage par le
 * {@link angeli.sprint.servlet.listener.ContextListener} et leurs methodes
 * {@link URL} sont enregistrees comme routes.
 * La classe doit avoir un constructeur sans argument.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
