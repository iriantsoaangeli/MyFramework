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
 * @date 2026/06/19 8:59
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Appele quand le context du servlet est initialisé
     * ,Initialized misy Z sode adino
     * 
     * @author Angeli
     * @date 2026/06/19 8:59
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        ClassPathScanner cpScanner = new ClassPathScanner();

        context.log("ContextListener : initialisation du context du servlet detecte");

        context.setAttribute("cpScanner", cpScanner);
        context.log("Le scanner de classe a ete ajoute au context du servlet");

        List<String> controllerList = cpScanner.scanPath(angeli.sprint.annotation.Controller.class);
        context.setAttribute("controllerList", controllerList);
        context.log("La liste des controllers a ete ajoute au context du servlet");

    }

}
