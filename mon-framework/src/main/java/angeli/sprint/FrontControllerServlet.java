package angeli.sprint;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        ProcessRequest(req, rep);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        ProcessRequest(req, rep);
    }

    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep) throws IOException {
            PrintWriter wr = rep.getWriter();
            String url = req.getRequestURL().toString();
            wr.print(url);
        
    }
}
