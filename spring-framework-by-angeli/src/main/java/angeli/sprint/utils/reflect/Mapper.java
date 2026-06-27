package angeli.sprint.utils.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class pour mapper des methodes/objets
 * 
 * @author Angeli
 */
public class Mapper {

    public Map<String, Method> mapUrlToMethod(List<Method> methods) {
        Map<String, Method> urlMap = new HashMap<>();
        for (Method method : methods){
            if(method.isAnnotationPresent(angeli.sprint.annotation.URL.class)){
                angeli.sprint.annotation.URL annotation = method.getAnnotation(angeli.sprint.annotation.URL.class);
                String url = annotation.value();
                urlMap.put(url, method);
            }
        }
        return urlMap;
    }
}
