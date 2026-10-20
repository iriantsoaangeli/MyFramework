package angeli.sprint.utils.reflect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ParamParser {

    /**
     * Parse les parametres de la requete pour recuperer les parametres d'un objet
     */
    static String[] parseArgs(String[] paramNames, String className, int profondeur) {
        Set<String> namesList = new HashSet<>();
        String split = "\\.";
        for (String string : paramNames) {
            // System.out
            //         .println("Parsing param: " + string + " for class: " + className + " at profondeur: " + profondeur);
            String[] parts = string.split(split);
            if (checkClosestEtage(profondeur, string, className) && parts.length > profondeur + 1)
                namesList.add(parts[profondeur + 1]);
        }

        // System.out.println("Params after parsing for class: " + className + " at profondeur: " + profondeur + " are: "
                // + String.join(", ", namesList));
        return namesList.toArray(new String[0]);
    }



    static boolean checkClosestEtage(int profondeur, String str, String classe) {
        if (profondeur >= str.split("\\.").length)
            return false;
        String name = str.split("\\.")[profondeur];
        if (name.equals(classe)) {
            System.out.println("Found closest etage for class: " + classe + " at profondeur: " + profondeur);
            return true;
        }
        System.out.println("Did not find closest etage for class: " + classe + " at profondeur: " + profondeur);
        return false;
    }

    /**
     * Recupere les parametres ayant ce prefix et cette profondeur
     */
    static String[] getParams(String[] list, String prefix, int profondeur) {
        List<String> ret = new ArrayList<String>();
        for (String string : list) {
            if (string.contains(prefix) && checkClosestEtage(profondeur, string, prefix)) {
                ret.add(string);
            }
        }
        return ret.toArray(new String[0]);
    }

    static int getArray(String[] tab) {
        String temp = "";
        for (int i = 0; i < tab.length; i++) {
            temp = tab[i].split("\\.")[1];

        }
        return -1;
    }

    /**
     * Cles (dans l'ordre de la requete) trouvees dans les cles de la forme
     * fullName[cle] ou fullName[cle].xxx , pour les Map
     */
    static Set<String> getKeys(Set<String> keys, String fullName) {
        Set<String> found = new java.util.LinkedHashSet<>();
        String start = fullName + "[";
        for (String key : keys) {
            if (!key.startsWith(start))
                continue;
            int end = key.indexOf(']', start.length());
            if (end < 0)
                continue;
            String rest = key.substring(end + 1);
            if (!(rest.isEmpty() || rest.startsWith(".")))
                continue;
            String k = key.substring(start.length(), end);
            if (k.isEmpty())
                continue;
            if (k.contains("."))
                throw new IllegalArgumentException(
                        "Cle invalide '" + k + "' dans " + key + " : le caractere '.' est interdit dans une cle de Map");
            found.add(k);
        }
        return found;
    }

    /**
     * Index (tries) trouves dans les cles de la forme fullName[i] ou fullName[i].xxx
     */
    static java.util.SortedSet<Integer> getIndexes(Set<String> keys, String fullName) {
        java.util.SortedSet<Integer> indexes = new java.util.TreeSet<>();
        String start = fullName + "[";
        for (String key : keys) {
            if (!key.startsWith(start))
                continue;
            int end = key.indexOf(']', start.length());
            if (end < 0)
                continue;
            String rest = key.substring(end + 1);
            String number = key.substring(start.length(), end);
            if ((rest.isEmpty() || rest.startsWith(".")) && TypeChecker.isNumber(number))
                indexes.add(Integer.valueOf(number));
        }
        return indexes;
    }
}