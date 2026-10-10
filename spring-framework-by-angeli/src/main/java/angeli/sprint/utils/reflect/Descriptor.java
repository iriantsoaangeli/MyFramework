package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Descriptor<T> {
    private Class<T> clazz;

    public Descriptor(Class<T> clazz) {
        this.clazz = clazz;
    }

     boolean doesFieldExist(String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public boolean isFieldSimple(String fieldName) throws NoSuchFieldException {
        if (!doesFieldExist(fieldName))
            return false;
        if (TypeChecker.isSimple(clazz.getDeclaredField(fieldName).getType())) {
            return true;
        }
        return false;
    }

    public Constructor<T> getConstructor(String[] paramNames) throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<T> matchingConstructor = null;
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == paramNames.length) {
                List<String> argNames = new ArrayList<>();
                for (int i = 0; i < constructor.getParameterCount(); i++) {
                    argNames.add(constructor.getParameters()[i].getName());
                }
                matchingConstructor = (Constructor<T>) constructor;
                for (String paramName : paramNames) {
                    if (!argNames.contains(paramName)) {
                        break;
                    }
                    return matchingConstructor;
                }
            }
        }
        throw new NoSuchMethodException("No matching constructor found");
    }
}