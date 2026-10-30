package angeli.sprint.utils.reflect.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Map;

import angeli.sprint.controller.ControllerManager;
import angeli.sprint.file.Fichier;
import angeli.sprint.session.Session;
import angeli.sprint.session.SessionManager;
import angeli.sprint.utils.reflect.Descriptor;
import angeli.sprint.utils.reflect.mapping.ParamBinder;

/**
 * Classe utilitaire pour faire la reflection : appelle une methode de
 * controller avec les arguments construits depuis la requete.
 */
public class Invoker {
    /**
     * Construit les arguments de la methode depuis la requete, instancie le
     * controller (constructeur vide) et appelle la methode.
     * Un parametre de type {@link Session} recoit la session du client, qui
     * est recopiee dans la HttpSession apres l'appel.
     *
     * @param method         la methode de controller
     * @param args           les parametres de la requete
     * @param sessionManager fournit la session
     * @param fichiers       les fichiers de la requete, peut etre null
     * @return la valeur retournee par la methode
     * @throws IllegalAccessException    si la methode n'est pas accessible
     * @throws InvocationTargetException si la methode lance une exception
     * @throws NoSuchMethodException     si un constructeur est introuvable
     * @throws InstantiationException    si un objet ne peut pas etre instancie
     * @throws ParseException            si une valeur ne peut pas etre convertie
     */
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
                // if (descriptor.getClazz().equals(byte[].class)) {
                //     System.out.println("Argument de type byte[] trouve, on le construit depuis les fichiers uploades"
                //             + descriptor.getArgName());
                //     newArgs[index] = fichiers.get(args.get(descriptor.getArgName())[0]).data();
                //     index++;
                //     continue;
                // }
                newArgs[index] = descriptor.createObject(args, null, fichiers);
                index++;
            }
            ret = method.invoke(ControllerManager.getController(method.getDeclaringClass()), newArgs);

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
     *
     * @param method la methode ou le constructeur
     * @param args   les parametres de la requete
     * @return les arguments convertis, dans l'ordre des parametres
     * @throws ParseException si une valeur ne peut pas etre convertie
     */
    static Object[] bindArgs(Executable method, Map<String, String[]> args) throws ParseException {
        Object[] convertedArgs = ParamBinder.bindInOrder(method, args);
        return convertedArgs;
    }

    /**
     * Instancie chaque constructeur avec ses arguments.
     *
     * @param args constructeur vers ses arguments
     * @return les objets crees
     * @throws IllegalArgumentException  si les arguments ne correspondent pas
     * @throws InstantiationException    si un objet ne peut pas etre instancie
     * @throws IllegalAccessException    si un constructeur n'est pas accessible
     * @throws InvocationTargetException si un constructeur lance une exception
     */
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
     *
     * @param values les valeurs d'un parametre
     * @return "array" si plusieurs valeurs, "single" sinon
     */
    static String getType(String[] values) {
        if (values.length > 1)
            return "array";
        else
            return "single";
    }

}
