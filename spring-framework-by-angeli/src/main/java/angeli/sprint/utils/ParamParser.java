package angeli.sprint.utils;

import java.util.ArrayList;
import java.util.List;

import angeli.sprint.utils.reflect.Descriptor;

public class ParamParser {
    public static List<Descriptor<?>> parseParams(String[] paramNames,Class<?>[] paramTypes) throws ClassNotFoundException {
        List<Descriptor<?>> descriptors = new ArrayList<Descriptor<?>>();
        for (Class<?> type : paramTypes) {
            descriptors.add(new Descriptor<>(type));
        }
        return descriptors;
    }
    static String[] parseNames(String[] paramNames) {
        List<String> namesList = new ArrayList<>();
        String split = ".";
        for (String string : paramNames) {
            namesList.add(string.split(split)[0]);
        }
        return namesList.toArray(new String[0]);
    }
}
