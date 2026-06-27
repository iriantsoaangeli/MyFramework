package angeli.sprint.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.classgraph.*;
import io.github.classgraph.MethodInfoList.MethodInfoFilter;

/**
 * La classe qui interagit avec les packages
 * 
 * @author Angeli
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
     * @see List<String> scanPath(Class<? extends Annotation>[]
     *      annotationClass, String... paths)
     *      pour plusieurs annotations
     * @param annotationClass l'annotation a chercher
     * @param paths           les paquets a scanner, exemple :
     *                        "angeli.sprint.controller",scan tout les paquets si
     *                        vide
     * @return clazzliste des classes avec l'annotation donnee,vide si rien est
     *         trouvee
     */
    public List<String> scanPathForClassAnnotation(Class<? extends Annotation> annotationClass,
            String... paths) {
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
     * Recupere les classes avec les annotations donnees dans les paquets donnes
     * Meme chose que
     * 
     * @see List<String> scanPath(Class<? extends Annotation>
     *      annotationClass, String... paths)
     *      pour une seule annotation
     * @param annotationClass l'annotation a chercher
     * @param paths           les paquets a scanner , exemple :
     *                        "angeli.sprint.controller", scan tout les paquets si
     *                        vide
     * @return clazz liste des classes avec l'annotation donnees,vide sinon
     */
    public List<String> scanPathForClassAnnotation(Class<? extends Annotation>[] annotationClass,
            String... paths) {
        List<String> clazz = new ArrayList<String>();
        try (ScanResult scanResult = classGraph.enableAllInfo().acceptPackages(paths).scan()) {
            ClassInfoList classInfoList = scanResult.getClassesWithAllAnnotations(annotationClass);
            for (ClassInfo classInfo : classInfoList) {
                clazz.add(classInfo.getName());
            }
        }
        return clazz;
    }

    /**
     * Recupere les methodes avec l'annotation donnee dans les classes donnes
     * @param classNames Une liste de classes en String 
     * @param annotationClass classe de l'annotation a chercher
     * @return Une liste de methodes avec l'annotation donnee sinon vide
     * @throws ClassNotFoundException il faut verifier la syntaxe 
     */
    public List<Method> scanClassForMethodAnnotation(List<String> classNames,
            Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        List<Method> methods = new ArrayList<Method>();
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            Method[] classMethods = clazz.getDeclaredMethods();
            System.out.println("classMethods: " + classMethods);
            System.out.println("className: " + className);
            for (Method method : classMethods) {
                method.setAccessible(true);
               Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    System.out.println("Method: " + method.getName() + ", Annotation: " + annotation.annotationType().getName());
                }
               if (method.isAnnotationPresent(annotationClass)) {
                    methods.add(method);
                }
            }
        }
        System.out.println("Methods with annotation " + annotationClass.getName() + ": " + methods);
        return methods;
    }
}
