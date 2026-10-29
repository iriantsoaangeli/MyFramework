package angeli.sprint.utils;

import java.lang.reflect.Method;
import java.util.Map;

import angeli.sprint.url.URLMethod;

/**
 * Regarde si une URL a une vue ou non, si elle existe ou non, et si elle
 * retourne un objet ou non
 */

public class URLChecker {

    /**
     * Analyse une URL par rapport aux routes connues.
     *
     * @param Url          le chemin demande
     * @param urlMethodMap les routes de la methode HTTP utilisee
     * @return [0] l'URL existe, [1] la methode retourne un ModelAndView,
     *         [2] la methode est une API ({@code @WebAPI} sans vue)
     */
    public static boolean[] checkUrl(String Url, Map<String, URLMethod> urlMethodMap) {
        boolean[] result = new boolean[3];
        result[0] = doesUrlExist(Url, urlMethodMap);
        if (!result[0]) {
            result[1] = false;
            result[2] = false;
        } else {
            result[1] = doesUrlHaveView(Url, urlMethodMap);
            result[2] = isUrlAnAPI(Url, urlMethodMap, result[1]);
        }
        return result;
    }

    /**
     * Retourne true si la methode est annotee {@code @WebAPI} et n'a pas de vue.
     *
     * @param url             le chemin demande, doit exister dans la map
     * @param urlMethodMap    les routes
     * @param doesUrlHaveView si la methode retourne un ModelAndView
     * @return true si la methode est annotee {@code @WebAPI} et n'a pas de vue
     */
    static boolean isUrlAnAPI(String url, Map<String, URLMethod> urlMethodMap, boolean doesUrlHaveView) {
        Method calledMethod = urlMethodMap.get(url).getMethod();
        boolean val = calledMethod.isAnnotationPresent(angeli.sprint.annotation.WebAPI.class)
                && !doesUrlHaveView;

        // Print si c'est une api ou non
        if (val)
            System.out.println("URL is an API");
        else
            System.out.println("URL is not an API");
        return val;
    }

    /**
     * Retourne le contentType du {@code @WebAPI} de la methode, "application/json" sinon.
     *
     * @param url          le chemin demande, doit exister dans la map
     * @param urlMethodMap les routes
     * @return le contentType du {@code @WebAPI} de la methode, "application/json" sinon
     */
    public static String getContentTypeForUrl(String url, Map<String, URLMethod> urlMethodMap) {
        Method calledMethod = urlMethodMap.get(url).getMethod();
        angeli.sprint.annotation.WebAPI annotation = calledMethod
                .getAnnotation(angeli.sprint.annotation.WebAPI.class);
        if (annotation != null) {
            return annotation.contentType();
        }
        return "application/json";

    }

    /**
     * Retourne true si la methode de l'URL retourne un ModelAndView.
     *
     * @param url          le chemin demande
     * @param urlMethodMap les routes
     * @return true si la methode de l'URL retourne un ModelAndView
     */
    static boolean doesUrlHaveView(String url, Map<String, URLMethod> urlMethodMap) {

        // Print url dont on veut savoir si elle a une vue dans catalina.out
        System.out.println("Checking if URL has view: " + url);

        if (urlMethodMap.containsKey(url)) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            System.out.println("Returning type of method: " + calledMethod.getReturnType().getName());
            if (calledMethod.getReturnType() == angeli.sprint.model.ModelAndView.class) {

                // Print dans catalina.out que la méthode a une vue
                System.out.println("URL has view: " + url);

                return true;
            }
        }

        // Print dans catalina.out que la méthode n'a pas de vue
        System.out.println("URL does not have view: " + url);

        return false;
    }

    /**
     * Retourne true si une route existe pour cette URL.
     *
     * @param url          le chemin demande
     * @param urlMethodMap les routes
     * @return true si une route existe pour cette URL
     */
    static boolean doesUrlExist(String url, Map<String, URLMethod> urlMethodMap) {
        if (urlMethodMap.containsKey(url)) {

            // Print si l'url existe dans catalina.out
            System.out.println("URL exists: " + url);
            return true;
        }
        return false;
    }
}