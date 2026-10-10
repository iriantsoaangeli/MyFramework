package angeli.sprint.utils;

import java.util.ArrayList;
import java.util.List;

import angeli.sprint.utils.reflect.Descriptor;

public class ParamParser {
    public static List<Descriptor<?>> parseParams(String[] paramNames,String[] paramTypes) throws ClassNotFoundException {
        List<Descriptor<?>> descriptors = new ArrayList<Descriptor<?>>();
        for (String type : paramTypes) {
            descriptors.add(new Descriptor<>(Class.forName(type)));
        }
        return descriptors;
    }

}
