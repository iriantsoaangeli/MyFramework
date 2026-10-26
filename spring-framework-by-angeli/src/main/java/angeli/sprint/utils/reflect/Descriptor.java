package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import angeli.sprint.file.Fichier;
import angeli.sprint.utils.ParamParser;
import angeli.sprint.utils.reflect.mapping.Convertisseur;
import angeli.sprint.utils.reflect.mapping.TypeChecker;

/**
 * Descripteur pour un parametre d'une methode (ou un champ de constructeur).
 * Sait construire la valeur du parametre a partir des parametres de la
 * requete : type simple, objet (via son constructeur, recursivement),
 * tableau, List, Map, {@link Fichier} ou byte[].
 */
public class Descriptor {
    /** Type du parametre. */
    private Class<?> clazz;
    /** Nom du parametre, avec son index pour un element de liste (nom[i]). */
    private String argName;
    /** Chemin du parent dans la requete (ex : "eleve.adresse"), vide a la racine. */
    private String prefix = null;
    /** Le type est un tableau ou une List. */
    boolean isArray = false;
    /** Le type est {@link Fichier}. */
    boolean isFichier = false;
    /** Le type est byte[] (contenu d'un fichier). */
    boolean isBytes = false;
    /** Type generique pour List et Map, null sinon. */
    private ParameterizedType type;
    /** Index utilise par {@link #updateIndex()}. */
    int index = 0;

    /**
     * Cree le descripteur et detecte s'il s'agit d'un tableau, d'une liste, d'un Fichier ou de byte[].
     *
     * @param clazz     type du parametre
     * @param parameter le parametre java, pour lire les types generiques ; peut etre null
     * @param argName   nom du parametre dans la requete
     * @param prefix    chemin du parent dans la requete, vide ou null a la racine
     */
    public Descriptor(Class<?> clazz, Parameter parameter, String argName, String prefix) {
        System.out.println("new descriptor : " + clazz.getName() + " , " + prefix + "." + argName);
        this.clazz = clazz;
        this.argName = argName;
        this.prefix = prefix;
        if ((List.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) && parameter != null
                && parameter.getParameterizedType() instanceof ParameterizedType pt)
            this.type = pt;
        this.isBytes = clazz.equals(byte[].class);
        this.isFichier = clazz.equals(Fichier.class);
        this.isArray = (clazz.isArray() || List.class.isAssignableFrom(clazz));
    }

    /**
     * Remplace l'index de argName par {@link #index} : nom[x] devient nom[index].
     */
    void updateIndex() {
        this.argName = argName.split("\\[")[0] + "[" + index + "]";
    }

    /**
     * Retourne le type du parametre.
     *
     * @return le type du parametre
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * Retourne le nom du parametre.
     *
     * @return le nom du parametre
     */
    public String getArgName() {
        return argName;
    }

    /**
     * Cherche le constructeur dont les noms de parametres sont exactement
     * ceux donnes (en ignorant les index [i]).
     * Necessite la compilation avec -parameters.
     *
     * @param paramNames les noms des champs envoyes dans la requete
     * @return le constructeur correspondant
     * @throws NoSuchMethodException si aucun constructeur ne correspond
     */
    public Constructor<?> getConstructor(String[] paramNames) throws NoSuchMethodException {
        // nom[0], nom[1]... correspondent a un seul parametre de constructeur : nom
        paramNames = Arrays.stream(paramNames).map(p -> p.split("\\[")[0]).distinct().toArray(String[]::new);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> matchingConstructor = null;
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == paramNames.length) {
                List<String> argNames = new ArrayList<>();
                for (int i = 0; i < constructor.getParameterCount(); i++) {
                    argNames.add(constructor.getParameters()[i].getName());
                }
                matchingConstructor = (Constructor<?>) constructor;
                boolean found = true;
                for (String paramName : paramNames) {
                    if (!argNames.contains(paramName.split("\\[")[0])) {

                        System.out.println("Constructor " + constructor + " does not match parameter names: "
                                + String.join(", ", paramNames));
                        found = false;
                        break;
                    }
                }
                if (found)
                    return matchingConstructor;
            }
        }
        throw new NoSuchMethodException("No matching constructor found for class " + clazz.getName()
                + " with parameter names: " + List.of(paramNames));
    }

    /**
     * Chemin complet de ce parametre dans la requete : prefix.argName
     *
     * @return argName seul si pas de prefix
     */
    private String fullName() {
        return (prefix == null || prefix.isEmpty()) ? argName : prefix + "." + argName;
    }

    /**
     * i-eme argument generique : {@code List<E>} : 0 pour E ; {@code Map<K,V>} : 0 pour K, 1 pour
     * V
     *
     * @param i l'index de l'argument generique
     * @return la classe de l'argument generique
     * @throws IllegalArgumentException si le type generique est absent ou imbrique
     */
    private Class<?> typeArgument(int i) {
        if (type != null && type.getActualTypeArguments().length > i
                && type.getActualTypeArguments()[i] instanceof Class<?> c)
            return c;
        throw new IllegalArgumentException(
                "Types generiques inconnus pour " + argName
                        + " : declarer List<Type> / Map<Cle,Valeur> (types simples ou classes, sans generique imbrique)");
    }

    /**
     * Construit les elements d'un tableau ou d'une liste.
     * Objets : nom[0].champ , nom[1].champ ...
     * Simples : nom[0] , nom[1] ... ou nom repete ({@code nom=a&nom=b})
     * Les index sont tries et compactes (0 et 2 donnent 2 elements)
     *
     * @param map      les parametres de la requete
     * @param etage    la profondeur courante
     * @param comp     le type des elements
     * @param fichiers les fichiers de la requete
     * @return les elements construits
     * @throws NoSuchMethodException     si un element n'a pas de constructeur adapte
     * @throws InstantiationException    si un element ne peut pas etre instancie
     * @throws IllegalAccessException    si le constructeur n'est pas accessible
     * @throws java.lang.reflect.InvocationTargetException si le constructeur lance une exception
     */
    List<Object> makeArray(Map<String, String[]> map, Integer etage, Class<?> comp, Map<String, Fichier> fichiers)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        String full = fullName();
        List<Object> elements = new ArrayList<>();
        if (TypeChecker.isSimple(comp)) {
            String[] values = map.get(full);
            if (values != null) {
                for (String value : values)
                    elements.add(Convertisseur.convert(value, comp));
            } else {
                for (Integer i : ParamParser.getIndexes(map.keySet(), full)) {
                    String[] indexed = map.get(full + "[" + i + "]");
                    if (indexed != null && indexed.length > 0)
                        elements.add(Convertisseur.convert(indexed[0], comp));
                }
            }
            return elements;
        }
        for (Integer i : ParamParser.getIndexes(map.keySet(), full)) {
            Descriptor element = new Descriptor(comp, null, argName + "[" + i + "]", prefix);
            elements.add(element.createObject(map, etage, fichiers));
        }
        return elements;
    }

    /**
     * Construit une Map. Format : nom[cle]=valeur (valeur simple) ou
     * nom[cle].champ=valeur (objet). La cle ne peut pas contenir . [ ou ] et doit
     * etre d'un type simple.
     *
     * @param map       les parametres de la requete
     * @param etage     la profondeur courante
     * @param keyType   le type des cles (type simple)
     * @param valueType le type des valeurs
     * @param fichiers  les fichiers de la requete
     * @return la Map construite, dans l'ordre de la requete
     * @throws NoSuchMethodException     si une valeur n'a pas de constructeur adapte
     * @throws InstantiationException    si une valeur ne peut pas etre instanciee
     * @throws IllegalAccessException    si le constructeur n'est pas accessible
     * @throws java.lang.reflect.InvocationTargetException si le constructeur lance une exception
     */
    Map<Object, Object> makeEntries(Map<String, String[]> map, Integer etage, Class<?> keyType, Class<?> valueType,
            Map<String, Fichier> fichiers)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        if (!TypeChecker.isSimple(keyType))
            throw new IllegalArgumentException(
                    "La cle de la Map " + argName + " doit etre d'un type simple, pas " + keyType.getName());
        String full = fullName();
        Map<Object, Object> entries = new LinkedHashMap<>();
        for (String k : ParamParser.getKeys(map.keySet(), full)) {
            Object key = Convertisseur.convert(k, keyType);
            if (TypeChecker.isSimple(valueType)) {
                String[] values = map.get(full + "[" + k + "]");
                if (values != null && values.length > 0)
                    entries.put(key, Convertisseur.convert(values[0], valueType));
            } else {
                Descriptor value = new Descriptor(valueType, null, argName + "[" + k + "]", prefix);
                entries.put(key, value.createObject(map, etage, fichiers));
            }
        }
        return entries;
    }

    /**
     * Construit la valeur de ce parametre a partir de la requete.
     * Type simple : converti depuis la valeur. Fichier / byte[] : pris dans les
     * fichiers. Tableau, List, Map : voir makeArray et makeEntries.
     * Objet : construit via le constructeur qui correspond aux champs envoyes.
     * Sans parametre dans la requete, un objet est cree avec son constructeur vide.
     *
     * @param map      les parametres de la requete
     * @param etage    la profondeur courante, null pour 0
     * @param fichiers les fichiers de la requete, peut etre null
     * @return la valeur construite
     * @throws NoSuchMethodException     si aucun constructeur ne correspond
     * @throws InstantiationException    si l'objet ne peut pas etre instancie
     * @throws IllegalAccessException    si le constructeur n'est pas accessible
     * @throws java.lang.reflect.InvocationTargetException si le constructeur lance une exception
     * @throws RuntimeException          si un parametre simple est absent de la requete
     */
    public Object createObject(Map<String, String[]> map, Integer etage, Map<String, Fichier> fichiers)
            throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, java.lang.reflect.InvocationTargetException {

        if (etage == null)
            etage = 0;

        if (TypeChecker.isSimple(clazz)) {
            String[] values = map.get(fullName());
            if (values != null && values.length > 0) {
                System.out.println("Creating simple instance of " + clazz.getName() + "associated with param name: "
                        + argName + " with values: " + List.of(values));
                return Convertisseur.convert(values[0], clazz);
            } else {
                throw new RuntimeException(
                        "Cannot create instance of " + clazz.getName() + ", param name: " + fullName()
                                + " not found in map");
            }
        }

        if (isBytes) {
            return fichiers.get(map.get(getArgName())[0]).data();
        }
        if (isFichier) {
            return fichiers.get(map.get(getArgName())[0]);
        }

        if (map == null || map.isEmpty())
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error creating instance of " + clazz.getName());
                throw new RuntimeException("Cannot create instance of " + clazz.getName());
            }

        if (clazz.isArray()) {
            Class<?> comp = clazz.getComponentType();
            List<Object> elements = makeArray(map, etage, comp, fichiers);
            Object array = java.lang.reflect.Array.newInstance(comp, elements.size());
            for (int i = 0; i < elements.size(); i++)
                java.lang.reflect.Array.set(array, i, elements.get(i));
            return array;
        }

        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList<Object>(makeArray(map, etage, typeArgument(0), fichiers));
        }

        if (Map.class.isAssignableFrom(clazz)) {
            return makeEntries(map, etage, typeArgument(0), typeArgument(1), fichiers);
        }

        System.out.println("Creating instance of " + clazz.getName() + " at etage " + etage);
        System.out.println("param names :" + List.of(map.keySet().toArray(new String[0])));

        String[] etageSuivant = ParamParser.parseArgs(map.keySet().toArray(new String[0]), argName, etage);

        return createObject(map, etage, etageSuivant, fichiers);
    }

    /**
     * Construit un objet avec le constructeur qui correspond aux noms de
     * l'etage suivant ; chaque argument est construit recursivement.
     *
     * @param map          les parametres de la requete
     * @param etage        la profondeur courante
     * @param etageSuivant les noms des champs de l'objet envoyes dans la requete
     * @param fichiers     les fichiers de la requete
     * @return l'objet construit
     * @throws NoSuchMethodException     si aucun constructeur ne correspond
     * @throws InstantiationException    si l'objet ne peut pas etre instancie
     * @throws IllegalAccessException    si le constructeur n'est pas accessible
     * @throws java.lang.reflect.InvocationTargetException si le constructeur lance une exception
     */
    Object createObject(Map<String, String[]> map, Integer etage, String[] etageSuivant, Map<String, Fichier> fichiers)
            throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, java.lang.reflect.InvocationTargetException {
        List<Object> args = new ArrayList<Object>();
        Constructor<?> constructor = getConstructor(etageSuivant);
        Parameter[] parameters = constructor.getParameters();
        for (Parameter parameter : parameters) {
            Descriptor descriptor = null;
            if (etage == 0) {
                descriptor = new Descriptor(parameter.getType(), parameter, parameter.getName(), argName);
            } else {
                descriptor = new Descriptor(parameter.getType(), parameter, parameter.getName(),
                        prefix + "." + argName);
            }
            Object arg = descriptor.createObject(map, etage + 1, fichiers);
            args.add(arg);
        }
        return constructor.newInstance(args.toArray());
    }

}