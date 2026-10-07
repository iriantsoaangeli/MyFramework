package angeli.sprint.utils;

import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Map;

import angeli.sprint.model.ModelAndView;
import angeli.sprint.url.URLMethod;

/**
 * 
 * URLChecker
 * Regarde si une URL a une vue ou non, si elle existe ou non, et si elle
 * retourne un objet ou non
 */

public class URLChecker {

    public static boolean[] checkUrl(String Url, Map<String, URLMethod> urlMethodMap) {
        boolean[] result = new boolean[3];
        result[0] = doesUrlExist(Url, urlMethodMap);
        result[1] = doesUrlHaveView(Url, urlMethodMap);
        result[2] = isUrlAnAPI(Url, urlMethodMap);
        return result;
    }

    static boolean isUrlAnAPI(String url, Map<String, URLMethod> urlMethodMap) {
        if (urlMethodMap.containsKey(url)) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            boolean val = calledMethod.isAnnotationPresent(angeli.sprint.annotation.WebAPI.class)
                    && !doesUrlHaveView(url, urlMethodMap);

            // Print si c'est une api ou non
            System.out.println("URL is an API");
            return val;
        }
        System.out.println("URL is not an API:");
        return false;
    }

    static String getContentTypeForUrl(String url, Map<String, URLMethod> urlMethodMap) {
        if (urlMethodMap.containsKey(url)) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            angeli.sprint.annotation.WebAPI annotation = calledMethod
                    .getAnnotation(angeli.sprint.annotation.WebAPI.class);
            if (annotation != null) {
                return annotation.contentType();
            }
            return "application/json";
        }
        return "application/json";
    }

    public static boolean doesUrlHaveView(String url, Map<String, URLMethod> urlMethodMap) {

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

    static boolean doesUrlExist(String url, Map<String, URLMethod> urlMethodMap) {
        if (urlMethodMap.containsKey(url)) {

            // Print si l'url existe dans catalina.out
            System.out.println("URL exists: " + url);
            return true;
        }
        return false;
    }
}