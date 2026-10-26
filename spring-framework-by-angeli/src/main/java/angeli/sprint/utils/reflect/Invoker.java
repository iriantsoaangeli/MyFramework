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

import angeli.sprint.file.Fichier;
import angeli.sprint.session.Session;
import angeli.sprint.session.SessionManager;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * Invoker
 * Classe utilitaire pour faire la reflection
 */
public class Invoker {
    public static Object invokeMethod(Method method, Map<String, String[]> args, SessionManager sessionManager,
            Map<String, Fichier> fichiers)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException,
            ParseException {
        boolean sessionParam = false;
        Session session = null;
        Object ret = null;

        try {
            // Construit les arguments pour la methode avec les parametres donnes
            System.out.println("Arguments peut etre construit depuis la requete ");
            Object[] newArgs = new Object[method.getParameterCount()];
            Descriptor[] descriptors = ParamBinder.getDescriptors(method);
            int index = 0;
            for (Descriptor descriptor : descriptors) {
                if (descriptor.getClazz().equals(Session.class)) {
                    session = sessionManager.getSession();
                    newArgs[index] = sessionManager.getSession();
                    sessionParam = true;
                    continue;
                }
                if (descriptor.getClazz().equals(byte[].class)) {
                    System.out.println("Argument de type byte[] trouve, on le construit depuis les fichiers uploades"+descriptor.getArgName());
                    newArgs[index] = fichiers.get(args.get(descriptor.getArgName())[0]).data();
                    index++;
                    continue;
                }
                newArgs[index] = descriptor.creaObject(args, null);
                index++;
            }
            ret = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(),
                    newArgs);

        } catch (Exception e2) {
            // Dis dans catalina.out si la methode ne peut pas etre invoquee avec les
            // arguments donnes
            System.out.println("Cannot invoke method" + method.getName());
            throw e2;
        }
        if (sessionParam) {
            sessionManager.updateSessionData(session);
        }
        return ret;

    }

    /**
     * Ne prend que les nombre primitifs ou non et les String
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

    /**
     * Dis si c'est un champ simple ou sinon un array ou une liste
     */
    static String getType(String[] values) {
        if (values.length > 1)
            return "array";
        else
            return "single";
    }

}
