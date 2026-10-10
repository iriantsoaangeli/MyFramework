package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Descriptor<T> {
    private Class<T> clazz;

    public Descriptor(Class<T> clazz) {
        this.clazz = clazz;
    }

    public boolean doesFieldExist(String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public boolean isFieldSimple(String fieldName) throws NoSuchFieldException{
        if (!doesFieldExist(fieldName))
            return false;
        if (TypeChecker.isSimple(clazz.getDeclaredField(fieldName).getType())) {
            return true;
        }
        return false;
    }
}