package angeli.sprint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.URLParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PageWriter {

    public static void urlNotFound(HttpServletResponse rep) throws IOException {
        PrintWriter wr = rep.getWriter();
        wr.println("<h1>error 404</h1>");
    }

    public static void printJson(PrintWriter wr, Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(o);
        wr.print(json);
    }

    public static void printHtml(PrintWriter wr, Object o) {
        wr.print(o);
    }

    public static void print(HttpServletRequest req, HttpServletResponse rep, Object message, String contentType)
            throws IOException {
        PrintWriter wr = rep.getWriter();

        if (contentType != null) {
            rep.setContentType(contentType);
            switch (contentType) {
                case "application/json":
                    printJson(wr, message);
                    break;

                case "text/html":
                    printHtml(wr, message);
                    break;
                default:
                    printJson(wr, message);
                    rep.setContentType("application/json");
                    break;
            }
        } else {
            printJson(wr, message);
            rep.setContentType("application/json");
        }
    }
}
