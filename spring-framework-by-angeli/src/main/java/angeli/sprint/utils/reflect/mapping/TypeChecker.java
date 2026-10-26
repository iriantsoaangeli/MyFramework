package angeli.sprint.utils.reflect.mapping;

import java.util.Map;
import java.util.function.Function;

/**
 * Tests sur les types et les valeurs des parametres.
 */
public class TypeChecker {
    /**
     * Retourne true si le type a un convertisseur (primitif, enveloppe, BigDecimal, BigInteger, String).
     *
     * @param clazz le type a tester
     * @return true si le type a un convertisseur (primitif, enveloppe, BigDecimal, BigInteger, String)
     */
    public static boolean isSimple(Class<?> clazz) {
        Map<Class<?>, Function<String, Object>> convertisseurs = Convertisseur.getConvertisseurs();
        return convertisseurs.containsKey(clazz);
    }

    /**
     * Retourne true si la valeur est un entier.
     *
     * @param value la valeur a tester
     * @return true si la valeur est un entier
     */
    public static boolean isNumber(String value) {
        try {
            Convertisseur.convert(value, Integer.class);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
