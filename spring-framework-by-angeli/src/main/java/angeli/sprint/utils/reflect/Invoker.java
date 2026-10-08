package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 
 * Invoker
 * Classe utilitaire pour faire la reflection
 */
public class Invoker {
    public static Object invokeMethod(Method method, Map<String, String[]> args)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        boolean canInvoke = canInvokeMethod(method, args);
        boolean canUseParams = ParamBinder.canBuildArgs(method.getParameterTypes(), args);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object ret = null;
        if (canInvoke) {
            // Dit dans catalina.out si la methode peut etre invoquee avec les arguments
            // donnes
            System.out.println("Can invoke method :" + method.getName());
            // ret =
            method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                    args);
        }
        if (canUseParams) {
            // Construit les arguments pour la methode avec les parametres donnes
            System.out.println("Argument peut etre construit depuis la requete ");
            ret = method.invoke(ParamBinder.constructObject(parameterTypes, args));
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
    static boolean areArgsValid(Executable method, Map<String, String[]> param) {
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
    static boolean canInvokeMethod(Method method, Map<String, String[]> args) {
        if (!doesItHaveArgs(method)) {
            return true;
        }
        if (areArgsValid(method, args))
            return true;
        return false;
    }

    static boolean canInvokeConstructor(Constructor<?> constructor, Object[] args) {
        return false;
    }


    // static boolean canConvert(Object from , Object to ){
        
    // }
}
