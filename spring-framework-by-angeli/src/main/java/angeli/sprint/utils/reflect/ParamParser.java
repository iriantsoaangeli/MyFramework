package angeli.sprint.utils.reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParamParser {

    /**
     * Parse les parametres de la requete pour recuperer les parametres d'un objet
     */
    static String[] parseArgs(String[] paramNames, String className, int profondeur) {
        List<String> namesList = new ArrayList<>();
        String split = "\\.";
        for (String string : paramNames) {
            System.out
                    .println("Parsing param: " + string + " for class: " + className + " at profondeur: " + profondeur);
            if (!string.split(split)[profondeur].isEmpty()
                    && checkClosestEtage(profondeur, string, className))
                namesList.add(string.split(split)[profondeur]);
        }

        System.out.println("Params after parsing for class: " + className + " at profondeur: " + profondeur + " are: "
                + String.join(", ", namesList));
        return namesList.toArray(new String[0]);
    }

    static boolean checkClosestEtage(int profondeur, String str, String classe) {
        int index = 1;
        String name = str.split("\\.")[index];
        if (name.equals(classe) && index == profondeur) {
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
}