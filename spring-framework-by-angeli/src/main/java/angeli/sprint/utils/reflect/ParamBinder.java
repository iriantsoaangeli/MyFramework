package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 
 * ParamBinder
 * Associe les parametres envoyes dans la requete avec les constructeurs
 * respecitfs de chacun des argument
 */
public class ParamBinder {
    public static Object[] constructObject(Class<?>[] clazz, Map<String, String[]> args)
            throws IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        return null;
    }

    static Constructor<?>[][] getConstructors(Class<?>[] clazz) {
        Constructor<?>[][] constructors = new Constructor[clazz.length][];
        for (int i = 0; i < clazz.length; i++) {
            constructors[i] = clazz[i].getDeclaredConstructors();
        }
        return constructors;
    }

    /**
     * Prend les valeurs dans la map et les met dans un Objet[] si il y a plusieurs
     * valeur sur un nom de parametre
     * 
     */
    static Object[] bindInOrder(Executable method, Map<String, String[]> args) {
        
        Object[] orderedArgs = new Object[args.size()];
        Integer index = 0;
        List<String> paramNames = Arrays.stream(method.getParameters()).map(p -> p.getName()).toList();
        Object[] paramTypes = method.getParameterTypes();
        for (String name : paramNames) {
            if (!args.containsKey(name))
                throw new IllegalArgumentException("Missing parameter " + name + " for method " + method.getName());
            else
                System.out.println("Binding param: " + name + " with value: " + args.get(name)[0] + " to type: "
                        + paramTypes[index]);
            orderedArgs[index] = Convertiesseur.convert(args.get(name)[0], (Class<?>) paramTypes[index]);
            index++;
        }
        return orderedArgs;
    }

    static Map<Constructor<?>, Object[]> findMatch(Constructor<?>[][] constructors, Map<String, String[]> args) {
        Map<Constructor<?>, Object[]> retMap = new java.util.HashMap<>();
        for (Constructor<?>[] construct : constructors) {
            for (Constructor<?> co : construct) {
                if (co.getParameterCount() > 0) {
                    String[] paramNames = Arrays.stream(co.getParameters()).map(p -> p.getName())
                            .toArray(String[]::new);
                    if (args.keySet().containsAll(Arrays.asList(paramNames))) {
                        retMap.put(co, bindInOrder(co, args));
                    }
                } else {
                    retMap.put(co, null);
                }
            }
        }
        return retMap;
    }
}