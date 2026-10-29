package angeli.sprint.url;

import java.lang.reflect.Method;

/**
 * Route enregistree : la methode de controller a appeler et la methode HTTP
 * qu'elle accepte.
 */
public class URLMethod {
    /** Methode de controller annotee {@link angeli.sprint.annotation.URL}. */
    Method method;
    /** Methode HTTP de la route ("GET" ou "POST"). */
    String requestMethod;

    /**
     * Cree une route.
     *
     * @param method        la methode de controller
     * @param requestMethod la methode HTTP ("GET" ou "POST")
     */
    public URLMethod(Method method, String requestMethod) {
        this.method = method;
        this.requestMethod = requestMethod;
    }

    /**
     * Retourne le nom de la methode et sa methode HTTP.
     *
     * @return le nom de la methode et sa methode HTTP
     */
    @Override
    public String toString(){
        String urlMethod = "Method: " + method.getName() + ", Request Method: " + requestMethod;
        return urlMethod;
    }

    /**
     * Retourne la methode de controller a invoquer.
     *
     * @return la methode de controller a invoquer
     */
    public Method getMethod(){
        return method;
    }

    /**
     * Retourne la methode HTTP de la route.
     *
     * @return la methode HTTP de la route
     */
    public String getRequestMethod(){
        return requestMethod;
    }

}
