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
        boolean canUseParams = ParamBinder.canBuildArgs(method.getParameterTypes(), args);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object ret = null;
        try {
            // Dit dans catalina.out si la methode peut etre invoquee avec les arguments
            // donnes
            System.out.println("Can invoke method :" + method.getName());
            if (doesItHaveArgs(method))
                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                        null);
            else
                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                        args);

        } catch (Exception e) {
            try {
                // Construit les arguments pour la methode avec les parametres donnes
                System.out.println("Argument peut etre construit depuis la requete ");
                ret = method.invoke(ParamBinder.constructObject(parameterTypes, args));
            } catch (Exception e2) {
                // Dis dans catalina.out si la methode ne peut pas etre invoquee avec les
                // arguments donnes
                System.out.println("Cannot invoke method" + method.getName());
                throw new IllegalArgumentException("Cannot invoke method with the given arguments.");
            }
        }
        return ret;

    }

    static Object invokeMethod(Executable method, Object[] args) {
        Object ret = null;
        return ret;
    }

    /**
     * Verifie si la methode a des arguments
     */
    static boolean doesItHaveArgs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length > 0;
    }

    static boolean canInvokeConstructor(Constructor<?> constructor, Object[] args) {
        return false;
    }

    // static boolean canConvert(Object from , Object to ){

    // }
}
