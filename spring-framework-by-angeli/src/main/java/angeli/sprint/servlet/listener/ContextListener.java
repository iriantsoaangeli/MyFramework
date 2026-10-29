package angeli.sprint.servlet.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import angeli.sprint.model.ModelAndView;
import angeli.sprint.session.SessionManager;
import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.reflect.classpath.ClassPathScanner;
import angeli.sprint.utils.reflect.mapping.Mapper;

/**
 * Listener du context des servlets.
 * Au demarrage de l'application : scanne le classpath pour trouver les
 * controllers, construit les routes GET/POST et met tout ce dont le
 * {@link angeli.sprint.servlet.FrontControllerServlet} a besoin dans le
 * context du servlet.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Appele quand le context du servlet est initialise.
     * Lance toutes les initialisations et branche {@link ModelAndView} sur le
     * context du servlet.
     *
     * @param sce l'evenement contenant le context du servlet
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        initPackageScanner(context);
        initMapper(context);
        initControllerList(context);
        initSuffixAndPrefix(context);
        initAuth(context);
        context.setAttribute("sessionManager", new SessionManager());

        ModelAndView.setAction((key, value) -> {
            context.setAttribute(key, value);
        });

        context.log("Le context du servlet a ete initialisé");
    }

    /**
     * Met le scanner de packages dans le context du servlet
     * Comme ca on peut l'utiliser dans le servlet
     *
     * @param context le context du servlet
     */
    void initPackageScanner(ServletContext context) {
        ClassPathScanner cpScanner = new ClassPathScanner();
        context.setAttribute("cpScanner", cpScanner);
        context.log("Le scanner de packages a ete ajoute au context du servlet");
    }

    /**
     * Met la liste des controllers dans le context du servlet
     * Met la liste des methodes avec l'annotation @URL dans le context du servlet
     *
     * @param context le context du servlet
     */
    void initControllerList(ServletContext context) {
        ClassPathScanner cpScanner = (ClassPathScanner) context.getAttribute("cpScanner");
        List<String> controllerList = cpScanner.scanPathForClassAnnotation(angeli.sprint.annotation.Controller.class);
        context.setAttribute("controllerList", controllerList);
        context.log("La liste des controllers a ete ajoute au context du servlet");
        mapUrlToMethod(context);
    }

    /**
     * Met le mapper dans le context du servlet
     *
     * @param context le context du servlet
     */
    void initMapper(ServletContext context) {
        Mapper mapper = new Mapper();
        context.setAttribute("mapper", mapper);
    }

    /**
     * Cree la liste des methodes avec l'annotation @URL et les associe avec leur
     * URL, separement pour GET et POST
     *
     * @param context le context du servlet
     * @throws RuntimeException si une classe controller est introuvable
     */
    @SuppressWarnings("unchecked")
    void mapUrlToMethod(ServletContext context) {

        ClassPathScanner cpScanner = (ClassPathScanner) context.getAttribute("cpScanner");
        List<String> controllerList = (List<String>) context.getAttribute("controllerList");
        Mapper mapper = (Mapper) context.getAttribute("mapper");

        try {
            List<Method> urlMethods = cpScanner.scanClassForMethodAnnotation(controllerList,
                    angeli.sprint.annotation.URL.class);
            context.setAttribute("urlMethods", urlMethods);
            context.log("La liste des méthodes annotées a été trouvee");

            Map<String, URLMethod> urlMethodMapPOST = mapper.mapUrlToMethodPOST(urlMethods);
            context.setAttribute("urlMethodMapPOST", urlMethodMapPOST);
            context.log("Les methodes POST ont ete mis dans le contexte avec leur urls");

            Map<String, URLMethod> urlMethodMapGET = mapper.mapUrlToMethodGET(urlMethods);

            context.setAttribute("urlMethodMapGET", urlMethodMapGET);
            context.setAttribute("urlMethodMapPOST", urlMethodMapPOST);

            context.log("Les methodes GET ont ete mis dans le contexte avec leur urls");

        } catch (ClassNotFoundException e) {
            context.log("Erreur lors de la récupération des classes pour chercher les URLs: " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des classes pour chercher les URLs", e);
        }
    }

    /**
     * Copie les parametres "prefix" et "suffix" du web.xml dans le context du
     * servlet, ils servent a construire le chemin des vues
     *
     * @param context le context du servlet
     */
    void initSuffixAndPrefix(ServletContext context) {
        context.setAttribute("prefix", context.getInitParameter("prefix"));
        context.setAttribute("suffix", context.getInitParameter("suffix"));
    }

    /**initialiser  */
    void initAuth(ServletContext context){
        context.setAttribute("angeli.spring.auth", null);
    }

}
