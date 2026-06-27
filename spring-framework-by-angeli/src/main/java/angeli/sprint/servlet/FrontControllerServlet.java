package angeli.sprint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    Map<String, Method> urlMethodMap;

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
        PrintWriter wr = rep.getWriter();
        String url = req.getRequestURL().toString();
        wr.print(url);
        wr.print("<Les Controllers trouves sont :");
        wr.print(controllerList);
        wr.print("<Les Methodes annotees avec @URL sont :");
        wr.print(methodList);
        wr.print("<Le Map URL -> Method  :");
        wr.print(urlMethodMap);
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
        urlMethodMap = (Map<String, Method>) getServletContext().getAttribute("urlMethodMap");
    }
}
