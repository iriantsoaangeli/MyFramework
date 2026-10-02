package angeli.sprint.utils;

import java.lang.reflect.Method;
import java.util.Map;

import angeli.sprint.model.ModelAndView;
import angeli.sprint.url.URLMethod;

/**
 * 
 * URLHandler
 * Regarde si une URL a une vue ou non, si elle existe ou non, et si elle
 * retourne un objet ou non
 */

public class URLHandler {
    public static boolean isUrlAnAPI(String url, Map<String, URLMethod> urlMethodMap) {
        if (urlMethodMap.containsKey(url)) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            boolean val = calledMethod.isAnnotationPresent(angeli.sprint.annotation.WebAPI.class)
                    && !(calledMethod.getReturnType().getClass().equals(ModelAndView.class));

            // Print la methode appelee
            System.out.println(calledMethod);
            return val;
        }
        return false;
    }

    public static String getContentTypeForUrl(String url, Map<String, URLMethod> urlMethodMap) {
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

    public static boolean doesUrlExist(String url, Map<String, URLMethod> urlMethodMap) {
        return urlMethodMap.containsKey(url);
    }
}