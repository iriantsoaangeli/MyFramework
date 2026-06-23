package angeli.sprint.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.classgraph.*;

/**
 * La classe qui interagit avec les packages
 * 
 * @author Angeli
 * @date 2026/6/16 11:44
 */
public class ClassPathScanner {

    /**
     * L'instance depuis la librairie ClassGraph pour scanner les paquets
     */
    ClassGraph classGraph;

    /**
     * Constructeur de la classe
     * Cree une classgraph dedans
     */
    public ClassPathScanner() {
        this.classGraph = new ClassGraph();
    }

    /**
     * Recupere les classes avec l'annotation donnee dans les paquets donnes
     * 
     * @see List<String> scanPath(Class<? extends java.lang.annotation.Annotation>[]
     *      annotationClass, String... paths)
     *      pour plusieurs annotations
     * @param annotationClass l'annotation a chercher
     * @param paths           les paquets a scanner
     * @return clazzliste des classes avec l'annotation donnee,vide si rien est trouvee
     */
    public List<String> scanPath(Class<? extends java.lang.annotation.Annotation> annotationClass, String... paths) {
        List<String> clazz = new ArrayList<String>();
        try (ScanResult scanResult = classGraph.enableAllInfo().acceptPackages(paths).scan()) {
            ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(annotationClass);
            for (ClassInfo classInfo : classInfoList) {
                clazz.add(classInfo.getName());
            }
        }
        return clazz;
    }

    /**
     * Recupere les classes avec les annotation donnees dans les paquets donnes
     * Meme chose que
     * 
     * @see List<String> scanPath(Class<? extends java.lang.annotation.Annotation>
     *      annotationClass, String... paths)
     *      pour une seule annotation
     * @param annotationClass l'annotation a chercher
     * @param paths           les paquets a scanner
     * @return clazz liste des classes avec l'annotation donnees,vide sinon
     */
    public List<String> scanPath(Class<? extends java.lang.annotation.Annotation>[] annotationClass, String... paths) {
        List<String> clazz = new ArrayList<String>();
        try (ScanResult scanResult = classGraph.enableAllInfo().acceptPackages(paths).scan()) {
            ClassInfoList classInfoList = scanResult.getClassesWithAllAnnotations(annotationClass);
            for (ClassInfo classInfo : classInfoList) {
                clazz.add(classInfo.getName());
            }
        }
        return clazz;
    }

}
