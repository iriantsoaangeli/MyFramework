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
    public static Object constructObject(Class<?>[] clazz, Object[] args) throws IllegalArgumentException {
        return null;
    }

    static boolean canIUseParams(Class<?>[] clazz, Object[] args) {
        int length = clazz.length;
        boolean canUse = true;
        Constructor<?>[] constructors = new Constructor<?>[length];
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
