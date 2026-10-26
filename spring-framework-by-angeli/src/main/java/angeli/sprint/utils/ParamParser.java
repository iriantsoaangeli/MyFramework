package angeli.sprint.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import angeli.sprint.utils.reflect.mapping.TypeChecker;

/**
 * Lit les noms des parametres de la requete pour les objets imbriques.
 * Les noms suivent la forme {@code objet.champ.sousChamp}, {@code liste[i].champ}
 * ou {@code map[cle].champ} ; la profondeur est l'index du morceau dans le nom.
 */
public class ParamParser {

    /**
     * Parse les parametres de la requete pour recuperer les parametres d'un objet.
     * Exemple : ["eleve.nom", "eleve.age"], "eleve", 0 donne ["nom", "age"].
     *
     * @param paramNames les noms des parametres de la requete
     * @param className  le nom attendu a la profondeur donnee
     * @param profondeur l'index du morceau a comparer
     * @return les noms (sans doublon) des champs de l'etage suivant
     */
    public static String[] parseArgs(String[] paramNames, String className, int profondeur) {
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



    /**
     * Retourne true si le morceau a cette profondeur est egal a classe.
     *
     * @param profondeur l'index du morceau a comparer
     * @param str        le nom du parametre (ex : "eleve.nom")
     * @param classe     le nom attendu
     * @return true si le morceau a cette profondeur est egal a classe
     */
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
     *
     * @param list       les noms des parametres
     * @param prefix     le nom attendu
     * @param profondeur l'index du morceau a comparer
     * @return les noms complets correspondants
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

    /**
     * Cles (dans l'ordre de la requete) trouvees dans les cles de la forme
     * fullName[cle] ou fullName[cle].xxx , pour les Map
     *
     * @param keys     les noms des parametres
     * @param fullName le nom complet de la Map
     * @return les cles trouvees, sans doublon
     * @throws IllegalArgumentException si une cle contient un '.'
     */
    public static Set<String> getKeys(Set<String> keys, String fullName) {
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
     *
     * @param keys     les noms des parametres
     * @param fullName le nom complet du tableau ou de la liste
     * @return les index trouves, tries
     */
    public static java.util.SortedSet<Integer> getIndexes(Set<String> keys, String fullName) {
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