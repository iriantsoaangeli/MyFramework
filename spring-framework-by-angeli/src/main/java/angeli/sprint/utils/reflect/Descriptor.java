package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Descriptor {
    Object fieldOwner;
    Field targetField;
    String [] paramNames ;
    Constructor<?> constructor;
}