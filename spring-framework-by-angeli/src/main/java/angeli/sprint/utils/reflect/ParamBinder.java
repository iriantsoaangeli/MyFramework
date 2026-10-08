package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import angeli.sprint.utils.reflect.Invoker;

/**
 * 
 * ParamBinder
 * Associe les parametres envoyes dans la requete avec les constructeurs
 * respecitfs de chacun des argument
 */
public class ParamBinder {
    public static Object[] constructObject(Class<?>[] clazz, Map<String, String[]> args)
            throws IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
            return null;
    }

    static boolean canUseParams(Class<?>[] clazz, Map<String, String[]> args) {
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
     * Prend une indexe du tableau d'arguments et verifie le tableau de param
     * jusqu'a depasser le max ou trouver tout les constructeurs
     */
    static void testArgsForConstructors(Class<?> clazz, Object[] args, Integer index) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {

        }
    }
}