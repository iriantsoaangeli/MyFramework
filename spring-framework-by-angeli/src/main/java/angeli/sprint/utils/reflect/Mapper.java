package angeli.sprint.utils.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Class pour mapper des methodes/objets
 * 
 * @author Angeli
 * @date 2026/6/23
 */
public class Mapper {
    ClassPathScanner cps;

    public Mapper(ClassPathScanner cps) {
        this.cps = cps;
    }
}
