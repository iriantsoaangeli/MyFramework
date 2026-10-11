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
            System.out
                    .println("Parsing param: " + string + " for class: " + className + " at profondeur: " + profondeur);
            if (!string.split(split)[profondeur].isEmpty()
                    && checkClosestEtage(profondeur, string, className))
                namesList.add(string.split(split)[profondeur + 1]);
        }

        System.out.println("Params after parsing for class: " + className + " at profondeur: " + profondeur + " are: "
                + String.join(", ", namesList));
        return namesList.toArray(new String[0]);
    }

    static boolean checkClosestEtage(int profondeur, String str, String classe) {
        if (profondeur > str.split("\\.").length)
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

    static List<Integer> getAllIndexes(String[] paramNames, String arrayName, int profondeur) {
        String split = "\\.";
        List<Integer> indexes = new ArrayList<>();
        for (String paramName : paramNames) {
            if (!paramName.split(split)[profondeur].isEmpty()
                    && paramName.split(split).length >= profondeur) {
                String cut = paramName.split(split)[profondeur];
                if(cut.contains(arrayName) && TypeChecker.isNumber(cut.split("\\[")[1].split("\\]")[0])){
                    indexes.add((Integer)Convertisseur.convert(cut.split("\\[")[1].split("\\]")[0], Integer.class));
                }
            }
        }
        return indexes;
    }

}