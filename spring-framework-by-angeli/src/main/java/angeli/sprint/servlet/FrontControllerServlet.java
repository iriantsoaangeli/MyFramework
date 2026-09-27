package angeli.sprint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
 * @author Angeli
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
        ProcessRequest(req, rep);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        ProcessRequest(req, rep);
    }

    /**
     * Print le URL sur la page web
     * 
     * @param req la requete http
     * @param rep la reponse http
     * @throws IOException
     */
    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep) throws IOException {

        if (req.getAttribute("jakarta.servlet.forward.request_uri") != null) {
            String targetPath = URLParser.getUrlFromRequest(req)[1];

            System.out.println("Forward detecte, on sert: " + targetPath);

            req.setAttribute("org.apache.catalina.jsp_file", targetPath);

            try {
                req.getServletContext().getNamedDispatcher("jsp").forward(req, rep);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return;
        }

        if (URLHandler.doesUrlExist(URLParser.getUrlFromRequest(req)[1], urlMethodMapGET)) {

            Method calledMethod = urlMethodMapGET.get(URLParser.getUrlFromRequest(req)[1]).getMethod();

            // Si l'url est un objet different de modelAndView
            if (URLHandler.isUrlObject(URLParser.getUrlFromRequest(req)[1], urlMethodMapGET)
                    && !URLHandler.doesUrlHaveView(URLParser.getUrlFromRequest(req)[1], urlMethodMapGET)) {
                try {
                    PageWriter.print(req, rep, calledMethod
                            .invoke(calledMethod.getDeclaringClass().getDeclaredConstructor().newInstance(), null),
                            URLHandler.getContentTypeForUrl(URLParser.getUrlFromRequest(req)[1], urlMethodMapGET));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (URLHandler.doesUrlHaveView(URLParser.getUrlFromRequest(req)[1], urlMethodMapGET)) {
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
            } else if (!URLHandler.isUrlObject(URLParser.getUrlFromRequest(req)[1], urlMethodMapGET)) {
                PageWriter.viewPageNotFound(req, rep, urlMethodMapGET, urlMethodMapPOST, controllerList, methodList);
            }

        } else {
            PageWriter.urlNotFound(rep);
        }
    }

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
