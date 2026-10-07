package angeli.sprint.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import angeli.sprint.model.ModelAndView;
import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.URLParser;
import angeli.sprint.utils.reflect.Invoker;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import angeli.sprint.utils.URLChecker;

/**
 * Servlet du spring-framework-by-Angeli
 * 
 * 
 */
public class FrontControllerServlet extends HttpServlet {

    /**
     * La liste des classes avec l'annotation @Controller
     */
    List<String> controllerList;
    List<Method> methodList;
    Map<String, URLMethod> urlMethodMapGET;
    Map<String, URLMethod> urlMethodMapPOST;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        ProcessRequest(req, rep, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        ProcessRequest(req, rep, "POST");
    }

    /**
     * Print le URL sur la page web
     * 
     * @param req la requete http
     * @param rep la reponse http
     * @throws IOException
     */
    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep, String method) throws IOException {
        System.out.println("===============================================================");

        String url = URLParser.getUrlFromRequest(req)[1];

        // Affiche la methode utilisee
        System.out.println("Method: " + method);

        // Affiche l'url sur la page web
        System.out.println("Application url : " + req.getContextPath() + url);

        Map<String, URLMethod> urlMethodMap = null;

        switch (method) {
            case "GET":
                urlMethodMap = urlMethodMapGET;
                break;

            case "POST":
                urlMethodMap = urlMethodMapPOST;
                break;
        }

        Object args[] = null;

        if (req.getParameterNames().hasMoreElements()) {
            args = req.getParameterMap().values().toArray();
        }

        boolean[] checkUrl = URLChecker.checkUrl(url, urlMethodMap);
        boolean UrlExists = checkUrl[0];
        boolean UrlHasView = checkUrl[1];
        boolean UrlisAnAPI = checkUrl[2];

        if (req.getAttribute("jakarta.servlet.forward.request_uri") != null) {
            String targetPath = url;

            // Afficher le path du jsp dans catalina.out
            System.out.println("Forward detecte, on sert: " + targetPath);
            req.setAttribute("org.apache.catalina.jsp_file", targetPath);
            try {
                req.getServletContext().getNamedDispatcher("jsp").forward(req, rep);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return;
        }
        if (UrlExists && !UrlHasView && !UrlisAnAPI) {
            // Dans ce cas si l'url a chemin et doit print du texte
            System.out.println("Ni vue ni API Mais Url Existe");
            PageWriter.print(req, rep, args, "text/html");
        } else if (UrlExists) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            // Si l'url est un objet different de modelAndView
            // Envoie en json
            if (UrlisAnAPI) {
                try {
                    PageWriter.print(req, rep, Invoker.invokeMethod(calledMethod, args),
                            URLChecker.getContentTypeForUrl(url, urlMethodMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (UrlHasView) {
                try {
                    ModelAndView modelAndView = (ModelAndView) Invoker.invokeMethod(calledMethod, args);

                    // Print le ModelAndView en String dans catalina.out
                    System.out.println("ModelAndView: " + modelAndView);

                    ServletContext context = req.getServletContext();
                    view(modelAndView.getView(), context.getAttribute("suffix").toString(),
                            context.getAttribute("prefix").toString(), req, rep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            PageWriter.urlNotFound(rep);
        }
    }

    /**
     * Affiche la vue sur la page web
     * 
     * @param viewName le nom du fichier jsp/html
     * @param suffix   le chemin du dossier des vues
     * @param prefix   .jsp ou .html
     * @throws IOException
     */
    public void view(String viewName, String suffix, String prefix, HttpServletRequest req, HttpServletResponse rep)
            throws IOException {
        String viewPath = suffix + viewName + prefix;

        // Print le chemin de la vue dans catalina.out
        System.out.println("View Path: " + viewPath);

        try {
            req.getRequestDispatcher(viewPath).forward(req, rep);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisation du servlet, recupere la liste des controllers depuis le
     * context du servlet
     */
    @Override
    public void init() throws ServletException {
        super.init();
        controllerList = (List<String>) getServletContext().getAttribute("controllerList");
        methodList = (List<Method>) getServletContext().getAttribute("urlMethods");
        urlMethodMapGET = (Map<String, URLMethod>) getServletContext().getAttribute("urlMethodMapGET");
        urlMethodMapPOST = (Map<String, URLMethod>) getServletContext().getAttribute("urlMethodMapPOST");
    }
}
