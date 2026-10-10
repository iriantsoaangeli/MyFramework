package angeli.sprint.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import angeli.sprint.utils.reflect.Descriptor;

public class ParamParser {

    public static String[] parseNames(String[] paramNames, int profondeur) {
        List<String> namesList = new ArrayList<>();
        String split = ".";
        for (String string : paramNames) {
            if (!string.split(split)[profondeur].isEmpty())
                namesList.add(string.split(split)[profondeur]);
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

    public static Map<String, String[]> next(Map<String, String[]> params) {
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
        int index = 0;
        for (String name : str.split(".")) {
            if (name.equals(classe) && index == profondeur) {
                return true;
            } else if (index == profondeur) {
                return false;
            }
        }
        return false;
    }

    public static String[] getParams(String[] list,String prefix,int profondeur){
        List<String> ret = new ArrayList<String>() ;
        for (String string : list) {
            if(string.contains(prefix)&& ){

            }
        }
    }
}