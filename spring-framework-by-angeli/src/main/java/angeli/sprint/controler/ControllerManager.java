package angeli.sprint.controler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * C'est a lui qu'on demande des controller , soi il initialise soi il te donne ce qu'il y a deja
 */
public class ControllerManager {
    /**
     * Une map des classes et de leurs instance
     */
    static Map<Class<?> , Object>  controllers = new HashMap<>();

    public static Object getInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Object controller = clazz.getDeclaredConstructor().newInstance();
        controllers.put(clazz, controller);
        return controller;
    }

    public static Object getController(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if(controllers.containsKey(clazz)) {
            return controllers.get(clazz);
        } else {
            return getInstance(clazz);
        }
    }


}
