package angeli.sprint.utils.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 
 * Reflector
 * Classe utilitaire pour faire la reflection
 */
public class Reflector {
    public static Object invokeMethod(Method method, Object[] args ) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException,InstantiationException{
        return method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args) ;
    }
}
