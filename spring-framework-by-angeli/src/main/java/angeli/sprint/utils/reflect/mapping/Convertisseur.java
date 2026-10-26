package angeli.sprint.utils.reflect.mapping;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Convertit les valeurs String de la requete vers les types simples.
 * Types geres : primitifs, enveloppes, BigDecimal, BigInteger et String.
 */
public class Convertisseur {

    /**
     * Retourne le tableau des valeurs converties.
     *
     * @param values     les valeurs a convertir
     * @param targetType le type tableau voulu (ex : int[].class)
     * @return le tableau des valeurs converties
     */
    static Object createArray(String[] values, Class<?> targetType) {
        Class<?> comp = targetType.getComponentType();
        Object array = java.lang.reflect.Array.newInstance(comp, values.length);
        for (int i = 0; i < values.length; i++) {
            java.lang.reflect.Array.set(array, i, convert(values[i], comp));
        }
        return array;
    }

    /**
     * Retourne une liste non modifiable si type est List, null sinon.
     *
     * @param values les valeurs
     * @param type   le type voulu
     * @return une liste non modifiable si type est List, null sinon
     */
    static Object createList(Object[] values, Class<?> type) {
        if (type == List.class && values != null) 
              return List.of(values);
        return null ;
    }

    /** Fonction de conversion par type. */
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

    /**
     * Retourne la valeur convertie.
     *
     * @param value      la valeur, non vide
     * @param targetType le type voulu
     * @return la valeur convertie
     * @throws IllegalArgumentException si le type n'est pas gere, si la valeur est vide
     *                                  ou mal formee (NumberFormatException)
     */
    public static Object convert(String value, Class<?> targetType) {
        Function<String, Object> convertisseur = CONVERTISSEURS.get(targetType);
        if (convertisseur != null && !value.isEmpty()) {
            return convertisseur.apply(value);
        } else {
            throw new IllegalArgumentException("Pas de convertisseur pour le type : " + targetType.getName());
        }
    }

    /**
     * Non implementee.
     *
     * @param map  les parametres de la requete
     * @param args les types voulus
     * @return un tableau de null de la taille de args
     */
    static Object[] createObjects(Map<String, String[]> map, Class<?>[] args) {
        Object[] objects = new Object[args.length];
        return objects;
    }

    /**
     * Retourne les convertisseurs par type.
     *
     * @return les convertisseurs par type
     */
    static Map<Class<?>, Function<String, Object>> getConvertisseurs() {
        return CONVERTISSEURS;
    }

}