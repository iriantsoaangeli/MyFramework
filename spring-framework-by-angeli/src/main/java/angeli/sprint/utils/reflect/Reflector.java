package angeli.sprint.utils.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * Reflector
 * Classe utilitaire pour faire la reflection
 */
public class Reflector {
    public static Object invokeMethod(Method method, Object[] args)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args);
    }

    /**
     * Verifie si les arguments sont valides pour la methode
     */
    static boolean areArgsValid(Method method, Object[] args) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].isInstance(args[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifie si la methode a des arguments
     */
    static boolean doesItHaveArgs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length > 0;
    }

    static boolean canInvokeMethod(Method method, Object[] args) {
        if (doesItHaveArgs(method))
            return true;
        if (areArgsValid(method, args)) {

        }
        return false;
    }
}
