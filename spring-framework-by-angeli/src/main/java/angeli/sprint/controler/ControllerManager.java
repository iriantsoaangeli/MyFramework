package angeli.sprint.controler;

import java.util.HashMap;
import java.util.Map;

/**
 * C'est a lui qu'on demande des controller , soi il initialise soi il te donne ce qu'il y a deja
 */
public class ControllerManager {
    /**
     * Une map des classes et de leurs instance
     */
    static Map<Class<?> , Object>  controllers = new HashMap<>();



}
