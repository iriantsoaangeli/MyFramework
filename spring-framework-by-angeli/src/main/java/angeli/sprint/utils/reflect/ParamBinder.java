package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import angeli.sprint.utils.reflect.Invoker;

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


    static Constructor<?>[] getConstructors(Class<?>[] clazz) {
        Constructor<?>[] constructors = new Constructor[clazz.length];
        for (int i = 0; i < clazz.length; i++) {
            constructors[i] = clazz[i].getConstructors()[0];
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
                System.out.println("Warning: Missing parameter " + name + " for method " + method.getName());
            else
                System.out.println("Binding param: " + name + " with value: " + args.get(name)[0] + " to type: "
                        + paramTypes[index]);
            orderedArgs[index] = Convertiesseur.convert(args.get(name)[0], (Class<?>) paramTypes[index]);
            index++;
        }
        return orderedArgs;
    }
}