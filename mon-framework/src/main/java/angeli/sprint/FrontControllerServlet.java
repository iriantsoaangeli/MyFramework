package angeli.sprint;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse rep) {
        ProcessRequest(req, rep);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse rep) {
        ProcessRequest(req, rep);
    }

    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep) {
        try {
            PrintWriter wr = rep.getWriter();
            String url = req.getRequestURL().toString();
            wr.print(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
