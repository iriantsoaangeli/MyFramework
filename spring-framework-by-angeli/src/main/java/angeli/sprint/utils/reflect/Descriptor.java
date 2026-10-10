package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Descriptor<T> {
    private Class<T> clazz;

    public Descriptor(Class<T> clazz) {
        this.clazz = clazz;
    }
}