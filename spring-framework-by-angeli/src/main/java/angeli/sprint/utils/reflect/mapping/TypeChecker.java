package angeli.sprint.utils.reflect.mapping;

import java.util.Map;
import java.util.function.Function;

public class TypeChecker {
    public static boolean isSimple(Class<?> clazz) {
        Map<Class<?>, Function<String, Object>> convertisseurs = Convertisseur.getConvertisseurs();
        return convertisseurs.containsKey(clazz);
    }

    public static boolean isNumber(String value) {
        try {
            Convertisseur.convert(value, Integer.class);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
