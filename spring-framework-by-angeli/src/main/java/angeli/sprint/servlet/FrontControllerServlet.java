package angeli.sprint.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import angeli.sprint.model.ModelAndView;
import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.URLParser;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import angeli.sprint.utils.URLHandler;

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

        String url = URLParser.getUrlFromRequest(req)[1];

        // Affiche la methode utilisee
        System.out.println("Method: " + method);

        Map<String, URLMethod> urlMethodMap = null;

        switch (method) {
            case "GET":
                urlMethodMap = urlMethodMapGET;
                break;

            case "POST":
                urlMethodMap = urlMethodMapPOST;
                break;
        }

        if (req.getAttribute("jakarta.servlet.forward.request_uri") != null) {
            String targetPath = url;

            System.out.println("Forward detecte, on sert: " + targetPath);

            req.setAttribute("org.apache.catalina.jsp_file", targetPath);

            try {
                req.getServletContext().getNamedDispatcher("jsp").forward(req, rep);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return;
        }

        if (URLHandler.doesUrlExist(url, urlMethodMap)) {

            Method calledMethod = urlMethodMap.get(url).getMethod();

            // Si l'url est un objet different de modelAndView
            if (URLHandler.isUrlObject(url, urlMethodMap)
                    && !URLHandler.doesUrlHaveView(url, urlMethodMap)) {
                try {
                    PageWriter.print(req, rep, calledMethod
                            .invoke(calledMethod.getDeclaringClass().getDeclaredConstructor().newInstance(), null),
                            URLHandler.getContentTypeForUrl(url, urlMethodMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (URLHandler.doesUrlHaveView(url, urlMethodMap)) {
                try {

                    ModelAndView modelAndView = (ModelAndView) calledMethod
                            .invoke(calledMethod.getDeclaringClass().getDeclaredConstructor().newInstance(), null);

                    // Print le ModelAndView en String dans catalina.out
                    System.out.println("ModelAndView: " + modelAndView);

                    ServletContext context = req.getServletContext();
                    view(modelAndView.getView(), context.getAttribute("suffix").toString(),
                            context.getAttribute("prefix").toString(), req, rep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!URLHandler.isUrlObject(url, urlMethodMap)) {
                PageWriter.viewPageNotFound(req, rep, urlMethodMapGET, urlMethodMapPOST, controllerList, methodList);
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
