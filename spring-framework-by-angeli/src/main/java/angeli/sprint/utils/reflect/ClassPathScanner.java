package angeli.sprint.utils.reflect;

import java.util.ArrayList;
import java.util.List;

import io.github.classgraph.*;

/**
 * La classe qui interagit avec les paquets
 * 
 * @author Angeli
 * @date 2026/6/16 11:44
 */
public class ClassPathScanner {
    
    ClassGraph classGraph;

    /**
     *Recupere les classes avec l'annotation donnee dans les paquets donnes
     * @param annotationClass l'annotation a chercher 
     * @param paths les paquets a scanner
     * @return liste des classes avec l'annotation donnees
     */
    public List<String> scanPath(Class<? extends java.lang.annotation.Annotation> annotationClass,String...paths) {
        List<String> clazz = new ArrayList<String>();

        return clazz;
    }
}
