package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * Invoker
 * Classe utilitaire pour faire la reflection
 */
public class Invoker {
    public static Object invokeMethod(Method method, Map<String, String[]> args)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException,
            ParseException {
        boolean canUseParams = ParamBinder.canBuildArgs(method.getParameterTypes(), args);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object ret = null;
        try {
            // Dit dans catalina.out si la methode peut etre invoquee avec les arguments
            // donnes
            System.out.println("Invoking method with given params :" + method.getName());
            if (!doesItHaveArgs(method)) {
                System.out.println("No arguments : Params go to hell");
                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                        null);
            } else {
                Object[] convertedArgs = bindArgs(method, args);

                //Print la list apres conversoin 
                System.out.print("List after conversion : ");
                for (Object arg : convertedArgs) {
                    System.out.print(arg+"as+"+arg.getClass()+"+,");
                }

                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                        convertedArgs);
            }

        } catch (Exception e) {

            System.out.print("Cannot invoke method " + method.getName() + " with given params : ");

            try {
            // Construit les arguments pour la methode avec les parametres donnes
            System.out.println("Argument peut etre construit depuis la requete ");
            ret = method.invoke(ParamBinder.constructObject(parameterTypes, args));
            } catch (Exception e2) {
            // Dis dans catalina.out si la methode ne peut pas etre invoquee avec les
            // arguments donnes
            System.out.println("Cannot invoke method" + method.getName());
            throw e2;
            }
        }
        return ret;

    }

    static Object[] bindArgs(Executable method, Map<String, String[]> args) throws ParseException {
        Object[] convertedArgs = ParamBinder.bindInOrder(method, args);
        return convertedArgs;
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
