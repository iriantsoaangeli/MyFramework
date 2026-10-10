package angeli.sprint.utils.reflect;

import java.util.Map;
import java.util.function.Function;

class TypeChecker {
    static boolean isSimple(Class<?> clazz) {
        Map<Class<?>, Function<String, Object>> convertisseurs = Convertisseur.getConvertisseurs();
        return convertisseurs.containsKey(clazz);

    }
}
