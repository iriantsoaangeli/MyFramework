package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * Invoker
 * Classe utilitaire pour faire la reflection
 */
public class Invoker {
    public static Object invokeMethod(Method method, Object[] args)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        boolean canInvoke = canInvokeMethod(method, args);
        boolean canUseParams = ParamMapper.canUseParams(method.getParameterTypes(), args);
        Class<?>[] parameterTypes = method.getParameterTypes();
       Object ret = null ;
        if (canInvoke) {
            // Dit dans catalina.out si la methode peut etre invoquee avec les arguments
            // donnes
            System.out.println("Can invoke method" + method.getName());
            ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args);
        }
        if (canUseParams) {
            // Construit les arguments pour la methode avec les parametres donnes
            System.out.println("Argument construit depuis la requete ");
            ret = method.invoke(ParamMapper.constructObject(parameterTypes, args));
        }
        // Dis dans catalina.out si la methode ne peut pas etre invoquee avec les
        // arguments donnes
        if (!canInvoke && !canUseParams) {
            System.out.println("Cannot invoke method" + method.getName());
            throw new IllegalArgumentException("Cannot invoke method with the given arguments.");
        }
        return ret;
    }

    /**
     * Verifie si les arguments sont valides pour la methode
     */
    static boolean areArgsValid(Executable method, Object[] args) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
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

    /**
     * Verifie si la methode peut etre invoquee avec les arguments donnes
     */
    static boolean canInvokeMethod(Method method, Object[] args) {
        if (!doesItHaveArgs(method)) {
            return true;
        }
        if (areArgsValid(method, args))
            return true;
        return false;
    }

    static boolean canInvokeConstructor(Constructor<?> constructor, Object[] args) {
        if (areArgsValid(constructor, args))
            return true;
        else
            return false;
    }

    static boolean doNumbersMatch(Constructor<?>[] cons, int nb) {
        int sum = 0;
        for (Constructor<?> co : cons) {
            sum = co.getParameterCount() + sum;
            if (sum > nb)
                return false;
        }
        return true;
    }
}
