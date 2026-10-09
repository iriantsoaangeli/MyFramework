package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
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

    static boolean canBuildArgs(Class<?>[] clazz, Map<String, String[]> args) {
        boolean canUse = true;
        // Dit dans catalina.out si les parametres peuvent etre utilises pour les
        // constructeurs donnes
        System.out.println("Can  use params: " + canUse);
        return canUse;
    }

    static boolean areTypesAssignable(Constructor<?> constructor, Object[] args) {
        return Invoker.canInvokeConstructor(constructor, args);
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
    static Object[] getParamValues(Map<String, String[]> args) {
        Object[] paramValues = new Object[args.size()];
        AtomicInteger index = new AtomicInteger(0);
        args.forEach((key, val) -> {
            if (val.length < 2)
                paramValues[index.get()] = args.get(key)[0];
            if (val.length >= 2)
                paramValues[index.get()] = args.get(key);
            index.incrementAndGet();
        });
        return paramValues;
    }

    static Object[] bindInOrder(Executable method, Map<String, String[]> args) {
        Object[] orderedArgs = new Object[args.size()];
        Integer index = 0;
        Parameter[] ps = method.getParameters(); // declaration order, guaranteed
        String[] paramNames = new String[ps.length];
        for (int i = 0; i < ps.length; i++) {
            paramNames[i] = ps[i].getName(); // "nom", "age"... (needs -parameters)
        }
        Object[] paramTypes = method.getParameterTypes();
        for (String name : paramNames) {
            if (!args.containsKey(name))
                System.out.println("Warning: Missing parameter " + name + " for method " + method.getName());
            else
                System.out.println("Binding param: " + name + " with value: " + args.get(name)[0] + " to type: "
                        + paramTypes[index]);
            if (paramTypes[index] instanceof Number)
                orderedArgs[index] = NumberFormat.getInstance().format(args.get(name)[0].toString());
            if (paramTypes[index] instanceof String)
                orderedArgs[index] = args.get(name)[0];
            index++;
        }
        return orderedArgs;
    }
}