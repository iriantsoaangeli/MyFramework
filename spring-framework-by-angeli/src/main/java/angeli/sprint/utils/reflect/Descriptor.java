package angeli.sprint.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Descripteur pour un parametre d'une methode
 */
public class Descriptor {
    private Class<?> clazz;
    private String argName;
    private String prefix = null;
    boolean isArray = false;
    private ParameterizedType type;
    int index = 0;

    public Descriptor(Class<?> clazz, Parameter parameter, String argName, String prefix) {
        System.out.println("new descriptor : " + clazz.getName() + " , " + prefix + "." + argName);
        this.clazz = clazz;
        this.argName = argName;
        this.prefix = prefix;
        if (List.class.isAssignableFrom(clazz) && parameter != null
                && parameter.getParameterizedType() instanceof ParameterizedType pt)
            this.type = pt;
        if (clazz.isArray() || List.class.isAssignableFrom(clazz))
            this.isArray = true;
    }

    void updateIndex() {
        this.argName = argName.split("\\[")[0] + "[" + index + "]";
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getArgName() {
        return argName;
    }

    public Constructor<?> getConstructor(String[] paramNames) throws NoSuchMethodException {
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
     */
    private String fullName() {
        return (prefix == null || prefix.isEmpty()) ? argName : prefix + "." + argName;
    }

    private Class<?> listElementType() {
        if (type != null && type.getActualTypeArguments()[0] instanceof Class<?> c)
            return c;
        throw new IllegalArgumentException(
                "Type des elements inconnu pour " + argName + " : declarer List<Type> et non List");
    }

    /**
     * Construit les elements d'un tableau ou d'une liste.
     * Objets : nom[0].champ , nom[1].champ ...
     * Simples : nom[0] , nom[1] ... ou nom repete (nom=a&nom=b)
     * Les index sont tries et compactes (0 et 2 donnent 2 elements)
     */
    List<Object> makeArray(Map<String, String[]> map, Integer etage, Class<?> comp)
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
            elements.add(element.creaObject(map, etage));
        }
        return elements;
    }

    Object creaObject(Map<String, String[]> map, Integer etage) throws NoSuchMethodException, InstantiationException,
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
                throw new RuntimeException("Cannot create instance of " + clazz.getName() + ", param name: " + fullName()
                        + " not found in map");
            }
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
            List<Object> elements = makeArray(map, etage, comp);
            Object array = java.lang.reflect.Array.newInstance(comp, elements.size());
            for (int i = 0; i < elements.size(); i++)
                java.lang.reflect.Array.set(array, i, elements.get(i));
            return array;
        }

        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList<Object>(makeArray(map, etage, listElementType()));
        }

        System.out.println("Creating instance of " + clazz.getName() + " at etage " + etage);
        System.out.println("param names :" + List.of(map.keySet().toArray(new String[0])));

        String[] etageSuivant = ParamParser.parseArgs(map.keySet().toArray(new String[0]), argName, etage);

        List<Object> args = new ArrayList<Object>();
        Constructor<?> constructor = getConstructor(etageSuivant);
        Parameter[] parameters = constructor.getParameters();

        for (Parameter parameter : parameters) {
            Descriptor descriptor = null;
            if (etage == 0)
                descriptor = new Descriptor(parameter.getType(), parameter, parameter.getName(), argName);
            else
                descriptor = new Descriptor(parameter.getType(), parameter, parameter.getName(),
                        prefix + "." + argName);

            Object arg = descriptor.creaObject(map, etage + 1);
            args.add(arg);
        }

        return constructor.newInstance(args.toArray());
    }
}