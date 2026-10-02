package angeli.sprint.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 
 * Reflector
 * Classe utilitaire pour faire la reflection
 */
public class Reflector {
    public static Object getMethodReturn(Method method, Object[] args ) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException,InstantiationException{
        return method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), args) ;
    }
}
