package angeli.sprint.utils.reflect;

import java.util.Map;
import java.util.function.Function;

class Convertiesseur {
    private static final Map<Class<?>, Function<String, Object>> CONVERTISSEURS = Map.ofEntries(
            // primitifs
            Map.entry(byte.class, Byte::valueOf),
            Map.entry(short.class, Short::valueOf),
            Map.entry(int.class, Integer::valueOf),
            Map.entry(long.class, Long::valueOf),
            Map.entry(float.class, Float::valueOf),
            Map.entry(double.class, Double::valueOf),
            Map.entry(boolean.class, Boolean::valueOf),
            Map.entry(char.class, s -> s.charAt(0)),

            // enveloppes
            Map.entry(Byte.class, Byte::valueOf),
            Map.entry(Short.class, Short::valueOf),
            Map.entry(Integer.class, Integer::valueOf),
            Map.entry(Long.class, Long::valueOf),
            Map.entry(Float.class, Float::valueOf),
            Map.entry(Double.class, Double::valueOf),
            Map.entry(Boolean.class, Boolean::valueOf),
            Map.entry(Character.class, s -> s.charAt(0)),

            // grands nombres (pas de valueOf(String))
            Map.entry(java.math.BigDecimal.class, java.math.BigDecimal::new),
            Map.entry(java.math.BigInteger.class, java.math.BigInteger::new),

            // String
            Map.entry(java.lang.String.class, java.lang.String::valueOf));

    static Object convert(String value, Class<?> targetType) {
        Function<String, Object> convertisseur = CONVERTISSEURS.get(targetType);
        if (convertisseur != null) {
            return convertisseur.apply(value);
        } else {
            throw new IllegalArgumentException("Pas de convertisseur pour le type : " + targetType.getName());
        }
    }

    static Object[] createObjects(Map<String, String[]> map, Class<?>[] args) {
        Object[] objects = new Object[args.length];
        return objects;
    }
}
