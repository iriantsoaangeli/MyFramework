package angeli.sprint.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * C'est a lui qu'on demande des controller , soi il initialise soi il te donne ce qu'il y a deja
 */
public class ControllerManager {
    /**
     * Une map des classes et de leurs instance
     */
    static Map<Class<?> , Object>  controllers = new ConcurrentHashMap<>();


    /**
     * @return Retourne une instance du controller de la classe donnée, en utilisant le constructeur par défaut.
     *         Si une instance existe déjà dans la map, elle est retournée. Sinon,
     */
    public static Object getInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Object controller = clazz.getDeclaredConstructor().newInstance();
        controllers.computeIfAbsent(clazz, k -> controller);
        return controller;
    }

    /**
     * Prend une instance de controller dans la map sinon en cree une et appel {@link #getInstance(Class)} pour l'initialiser
     */
    public static Object getController(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if(controllers.containsKey(clazz)) {
            return controllers.get(clazz);
        } else {
            return getInstance(clazz);
        }
    }


}
