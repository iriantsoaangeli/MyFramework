package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
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
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object ret = null;

        //Cas 1 : Type primitif,nombre,String , ou pas de parametre 
        try {

            // Dit dans catalina.out si la methode peut etre invoquee avec les arguments
            // donnes
            System.out.println("Invoking method with given params :" + method.getName());
            if (!(method.getParameterCount() > 0)) {
                System.out.println("No arguments : Params go to hell");
                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                        null);
            } else {
                Object[] convertedArgs = bindArgs(method, args);

                // Print la list apres conversoin
                System.out.print("List after conversion : ");
                for (Object arg : convertedArgs) {
                    System.out.print(arg + " : " + arg.getClass() + ",");
                }

                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                        convertedArgs);
            }

            //Cas 2 : Type complexe (objet) , on essaye de construire l'objet avec les parametres donnes
        } catch (Exception e) {

            System.out.print("Cannot invoke method " + method.getName() + " with given params : ");

            try {
                // Construit les arguments pour la methode avec les parametres donnes
                System.out.println("Arguments peut etre construit depuis la requete ");
                Map<Constructor<?>, Object[]> constructorsAndArgs = ParamBinder.getConstructorsAndArgs(parameterTypes,
                        args);
                Object[] newArgs = createArgs(constructorsAndArgs);
                ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), newArgs);
            } catch (Exception e2) {
                // Dis dans catalina.out si la methode ne peut pas etre invoquee avec les
                // arguments donnes
                System.out.println("Cannot invoke method" + method.getName());
                throw e2;
            }
        }
        return ret;

    }

    /**
     * Ne prend que les nombre primitifs ou non  et les String 
     */
    static Object[] bindArgs(Executable method, Map<String, String[]> args) throws ParseException {
        Object[] convertedArgs = ParamBinder.bindInOrder(method, args);
        return convertedArgs;
    }

    static Object[] createArgs(Map<Constructor<?>, Object[]> args)
            throws IllegalArgumentException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Object[] ret = new Object[args.size()];
        int i = 0;
        for (Map.Entry<Constructor<?>, Object[]> entry : args.entrySet()) {
            ret[i] = entry.getKey().newInstance(entry.getValue());
            i++;
        }
        return ret;
    }

    static String getType(String[] values){
        if(values.length > 1)
            return "array";
        else
            return "single";
    }

}
