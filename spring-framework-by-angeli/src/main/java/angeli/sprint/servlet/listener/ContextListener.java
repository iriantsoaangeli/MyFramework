package angeli.sprint.servlet.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

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
        initController(context);

        context.log("Le context du servlet a ete initialisé");
    }


    /**
     * Met le scanner de pages dans le context du servlet
     * Comme ca on peut l'utiliser dans le servlet
     */
    void initPackageScanner(ServletContext context){
        ClassPathScanner cpScanner = new ClassPathScanner();
        context.setAttribute("cpScanner", cpScanner);
        context.log("Le scanner de packages a ete ajoute au context du servlet");
    }

    /**
     * Met la liste des controllers dans le context du servlet
     */
    void initController(ServletContext context){
        ClassPathScanner cpScanner = (ClassPathScanner) context.getAttribute("cpScanner");
        List<String> controllerList = cpScanner.scanPathForClassAnnotation(angeli.sprint.annotation.Controller.class);
        context.setAttribute("controllerList", controllerList);
        context.log("La liste des controllers a ete ajoute au context du servlet");
    }
}
