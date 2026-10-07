package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import angeli.sprint.utils.reflect.Invoker;

/**
 * 
 * ParamMapper
 * Associe les parametres envoyes dans la requete avec les constructeurs
 * respecitfs de chacun des argument
 */
public class ParamMapper {
    public static Object[] constructObject(Class<?>[] clazz, Object[] args)
            throws IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        if (canUseParams(clazz, args)) {
            Object[] result = new Object[clazz.length];
            int i = 0;
            int startarg = 0;
            int endarg = 0;
            for (Class<?> c : clazz) {
                Constructor<?> constructor = c.getConstructor();
                endarg += constructor.getParameterCount() + startarg - 1;
                Object[] argsToTry = Arrays.copyOfRange(args, startarg, endarg);
                result[i] = constructor.newInstance(argsToTry);
                i++;
            }
        }
        return null;
    }

    static boolean canUseParams(Class<?>[] clazz, Object[] args) {
        int length = clazz.length;
        boolean canUse = true;
        Constructor<?>[] constructors = getConstructors(clazz) ;
        int endarg = 0;
        int startarg = 0;
        for (int i = 0; i < length; i++) {
            if (constructors[i].getParameterCount() == 0)
                canUse = canUse && true;
            else {
                endarg = constructors[i].getParameterCount() + startarg - 1;
                Object[] argsToTry = Arrays.copyOfRange(args, startarg, endarg);
                canUse = canUse && areTypesAssignable(constructors[i], argsToTry);
            }
            if (!canUse)
                break;
        }
        // Dit dans catalina.out si les parametres peuvent etre utilises pour les constructeurs donnes
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
}
