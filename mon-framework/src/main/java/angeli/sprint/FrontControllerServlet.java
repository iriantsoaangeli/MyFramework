package angeli.sprint;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet du spring-framework-by-Angeli
 * @author Angeli
 * @date 2024/6/11 17:28
 */
public class FrontControllerServlet extends HttpServlet {
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
     * @param req la requete http
     * @param rep la reponse http
      * @throws IOException
     */
    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep) throws IOException {
            PrintWriter wr = rep.getWriter();
            String url = req.getRequestURL().toString();
            wr.print(url);
    }
}
