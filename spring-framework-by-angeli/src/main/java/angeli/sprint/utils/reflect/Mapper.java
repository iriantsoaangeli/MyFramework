package angeli.sprint.utils.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import angeli.sprint.url.URLMethod;

/**
 * Class pour mapper des methodes/objets
 * 
 * @author Angeli
 */
public class Mapper {

    /**
     * Map les methodes avec l'annotation @URL avec leur URL
     * 
     * @param methods liste de methodes donnees
     * @return map avec l'URL comme cle et la methode comme valeur
     */
    public Map<String, URLMethod> mapUrlToMethod(List<Method> methods) {
        Map<String, URLMethod> urlMap = new HashMap<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(angeli.sprint.annotation.URL.class)) {
                angeli.sprint.annotation.URL annotation = method.getAnnotation(angeli.sprint.annotation.URL.class);
                String url = annotation.value();
                if (urlMap.containsKey(url) && urlMap.get(url).getRequestMethod().equals(annotation.method())) {
                    throw new RuntimeException("Doublons sur URL ET METHODE pour la methode " + method.getName() + " et la methode " + urlMap.get(url).getMethod().getName());
                } else {
                    urlMap.put(url, new URLMethod(method, annotation.method()));
                }
            }
        }
        return urlMap;
    }
}
