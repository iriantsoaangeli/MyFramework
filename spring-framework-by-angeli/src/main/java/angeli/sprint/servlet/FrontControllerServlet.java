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
    Map<String, URLMethod> urlMethodMap;

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
        rep.setContentType("text/html;charset=UTF-8");
        PrintWriter wr = rep.getWriter();
        String[] url = URLParser.getUrlFromRequest(req);
        String uri = url[1];
        URLMethod methodPresent = urlMethodMap.get(uri);
        urlMethodMap.remove(uri);
        wr.println("<h2>"+url[0]+" "+url[1]+"</h2>");
        wr.println("<h3>Les Controllers trouves sont :</h3>");
        wr.println(controllerList);
        wr.println("<h3>Les Methodes annotees avec @URL sont :</h3>");
        wr.println(methodList);
        wr.println("<h3>Le Map URL -> Method  :</h3>");
        wr.println(urlMethodMap);
        urlMethodMap.put(url[1], methodPresent);
        wr.println("<h3>Votre url : " + url[0] + " " + url[1] + "</h3>");
        wr.println("<h3>Methode appellee :" + methodPresent.getMethod().getName() + "()</h3>");
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
        urlMethodMap = (Map<String, URLMethod>) getServletContext().getAttribute("urlMethodMap");
    }
}
