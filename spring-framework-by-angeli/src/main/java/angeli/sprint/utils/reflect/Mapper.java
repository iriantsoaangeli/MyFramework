package angeli.sprint.utils.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class Mapper {
    ClassPathScanner cps;

    public Mapper(ClassPathScanner cps) {
        this.cps = cps;
    }

    public HashMap<String,Method> mapMethodWithURL(List<String> classList){
        HashMap<String,Method> map = new HashMap<>();

        return map;
    }
}
