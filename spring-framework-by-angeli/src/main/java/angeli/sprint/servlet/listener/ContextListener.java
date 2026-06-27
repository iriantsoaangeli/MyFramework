package angeli.sprint.servlet.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.lang.reflect.Method;
import java.util.List;

import angeli.sprint.utils.reflect.ClassPathScanner;

/**
 * Listner du context des servlets
 * 
 * @author Angeli
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Appele quand le context du servlet est initialisé
     * ,Initialized misy Z sode adino
     * 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        initPackageScanner(context);
        initControllerList(context);

        context.log("Le context du servlet a ete initialisé");
    }

    /**
     * Met le scanner de pages dans le context du servlet
     * Comme ca on peut l'utiliser dans le servlet
     */
    void initPackageScanner(ServletContext context) {
        ClassPathScanner cpScanner = new ClassPathScanner();
        context.setAttribute("cpScanner", cpScanner);
        context.log("Le scanner de packages a ete ajoute au context du servlet");
    }

    /**
     * Met la liste des controllers dans le context du servlet
     * Met la liste des methodes avec l'annotation @URL dans le context du servlet
     */
    void initControllerList(ServletContext context) {
        ClassPathScanner cpScanner = (ClassPathScanner) context.getAttribute("cpScanner");
        List<String> controllerList = cpScanner.scanPathForClassAnnotation(angeli.sprint.annotation.Controller.class);
        context.setAttribute("controllerList", controllerList);
        context.log("La liste des controllers a ete ajoute au context du servlet");
        try {
            List<Method> urlMethods = cpScanner.scanClassForMethodAnnotation(controllerList,
                    angeli.sprint.annotation.URL.class);
            context.setAttribute("urlMethods", urlMethods);
            context.log("La liste des méthodes annotées a été ajoutée au contexte du servlet");
        } catch (ClassNotFoundException e) {
            context.log("Erreur lors de la récupération des classes pour chercher les URLs: " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des classes pour chercher les URLs", e);
        }
    }

}
