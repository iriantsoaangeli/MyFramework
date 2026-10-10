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
        String split = ".";
        for (String string : paramNames) {
            if (!string.split(split)[profondeur].isEmpty() && string.contains(className)
                    && checkClosestEtage(profondeur, string, className))
                namesList.add(string.split(split)[profondeur-1]);
        }

        return namesList.toArray(new String[0]);
    }

    static String nextEtage(String name) {
        String nouveauString = name.substring(name.indexOf(".") + 1);
        if (nouveauString.isBlank()) {
            return null;
        } else {
            return nouveauString;
        }
    }

     static Map<String, String[]> next(Map<String, String[]> params) {
        Map<String, String[]> nextParams = new HashMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (nextEtage(entry.getKey()) != null)
                nextParams.put(nextEtage(entry.getKey()), entry.getValue());
        }
        if (nextParams.isEmpty())
            return null;
        return nextParams;
    }

    static boolean checkClosestEtage(int profondeur, String str, String classe) {
        int index = 1;
        for (String name : str.split(".")) {
            if (name.equals(classe) && index == profondeur) {
                return true;
            } else if (index == profondeur) {
                return false;
            }
            index++;
        }
        return false;
    }

    /**
     * Recupere les parametres  ayant ce prefix et cette profondeur
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