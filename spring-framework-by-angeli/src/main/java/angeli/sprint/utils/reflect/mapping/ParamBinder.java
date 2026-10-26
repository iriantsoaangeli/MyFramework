package angeli.sprint.utils.reflect.mapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import angeli.sprint.utils.reflect.Descriptor;

/**
 * Associe les parametres envoyes dans la requete avec les constructeurs
 * respectifs de chacun des arguments
 */
public class ParamBinder {

    /**
     * Retourne les constructeurs declares de chaque classe.
     *
     * @param clazz les classes
     * @return les constructeurs declares de chaque classe
     */
    static Constructor<?>[][] getConstructors(Class<?>[] clazz) {
        Constructor<?>[][] constructors = new Constructor[clazz.length][];
        for (int i = 0; i < clazz.length; i++) {
            constructors[i] = clazz[i].getDeclaredConstructors();
        }
        return constructors;
    }

    /**
     * Prend les valeurs dans la map et les met dans un Objet[] si il y a plusieurs
     * valeur sur un nom de parametre.
     * Seule la premiere valeur de chaque parametre est utilisee.
     *
     * @param method la methode ou le constructeur
     * @param args   les parametres de la requete
     * @return les valeurs converties, dans l'ordre des parametres
     * @throws IllegalArgumentException si un parametre manque dans la requete
     */
    public static Object[] bindInOrder(Executable method, Map<String, String[]> args) {

        Object[] orderedArgs = new Object[args.size()];
        Integer index = 0;
        List<String> paramNames = Arrays.stream(method.getParameters()).map(p -> p.getName()).toList();
        Object[] paramTypes = method.getParameterTypes();
        for (String name : paramNames) {
            if (!args.containsKey(name))
                throw new IllegalArgumentException("Missing parameter " + name + " for method " + method.getName());
            else
                System.out.println("Binding param: " + name + " with value: " + args.get(name)[0] + " to type: "
                        + paramTypes[index]);
            orderedArgs[index] = Convertisseur.convert(args.get(name)[0], (Class<?>) paramTypes[index]);
            index++;
        }
        return orderedArgs;
    }

    /**
     * Garde les constructeurs dont tous les parametres sont dans la requete
     * (et les constructeurs vides, avec null comme arguments).
     *
     * @param constructors les constructeurs candidats
     * @param args         les parametres de la requete
     * @return constructeur vers ses arguments
     */
    static Map<Constructor<?>, Object[]> findMatch(Constructor<?>[][] constructors, Map<String, String[]> args) {
        Map<Constructor<?>, Object[]> retMap = new java.util.HashMap<>();
        for (Constructor<?>[] construct : constructors) {
            for (Constructor<?> co : construct) {
                if (co.getParameterCount() > 0) {
                    String[] paramNames = Arrays.stream(co.getParameters()).map(p -> p.getName())
                            .toArray(String[]::new);
                    if (args.keySet().containsAll(Arrays.asList(paramNames))) {
                        retMap.put(co, bindInOrder(co, args));
                    }
                } else {
                    retMap.put(co, null);
                }
            }
        }
        return retMap;
    }

    /**
     * Retourne un tableau de Descripteur pour chaque parametre de la methode
     *
     * @param method la methode ou le constructeur
     * @return un descripteur par parametre, dans l'ordre
     */
    public static Descriptor[] getDescriptors(Executable method) {
        Parameter[] parameters = method.getParameters();
        List<Descriptor> descriptors = new ArrayList<>();
        for (Parameter parameter : parameters) {
            descriptors.add(new Descriptor(parameter.getType(), parameter, parameter.getName(), ""));
        }
        return descriptors.toArray(new Descriptor[0]);
    }
}
