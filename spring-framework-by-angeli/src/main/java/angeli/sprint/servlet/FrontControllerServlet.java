package angeli.sprint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.URLParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    Map<String, URLMethod> urlMethodMapPost;

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
     * @date 2026/6/11 17:29
     */
    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        if(doesUrlExist(req.getRequestURL().toString(), urlMethodMapGET)) {
            // Handle existing URL
        } else {
            PageWriter.viewPageNotFound(req, rep, urlMethodMapGET, controllerList, methodList);
        }
    }

    public boolean doesUrlExist(String url, Map<String, URLMethod> urlMethodMap) {
        return urlMethodMap.containsKey(url);
    }

    public void view(HttpServletRequest req, HttpServletResponse rep, String viewName) throws ServletException, IOException {
        String prefix = (String) getServletContext().getAttribute("prefix");
        String affix = (String) getServletContext().getAttribute("affix");
        String fullViewPath = prefix + viewName + affix;
        req.getRequestDispatcher(fullViewPath).forward(req, rep);
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
        urlMethodMapPost = (Map<String, URLMethod>) getServletContext().getAttribute("urlMethodMapPOST");
    }
}
