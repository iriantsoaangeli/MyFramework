package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;

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
        Constructor<?>[] constructor = null;
        return false;
    }

    

    static Constructor<?>[] getConstructors(Class<?>[] clazz) {
        Constructor<?>[] constructors = new Constructor[clazz.length];
        for (int i = 0; i < clazz.length; i++) {
            constructors[i] = clazz[i].getConstructors()[0];
        }
        return constructors;
    }
}
