package angeli.sprint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.URLParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PageWriter {
    public static void viewPageNotFound(HttpServletRequest req, HttpServletResponse rep,
            Map<String, URLMethod> urlMethodMapGET, List<String> controllerList, List<Method> methodList)
            throws IOException {
        rep.setContentType("text/html;charset=UTF-8");
        PrintWriter wr = rep.getWriter();
        String method = req.getMethod();
        String[] url = URLParser.getUrlFromRequest(req);
        String uri = url[1];
        URLMethod methodPresent = urlMethodMapGET.get(uri);
        urlMethodMapGET.remove(uri);
        wr.println("<h2>" + url[0] + " " + url[1] + "</h2>");
        wr.println("<h3>Les Controllers trouves sont :</h3>");
        wr.println(controllerList);
        wr.println("<h3>Les Methodes annotees avec @URL sont :</h3>");
        wr.println(methodList);
        wr.println("<h3>Le Map URL -> Method  :</h3>");
        wr.println(urlMethodMapGET);
        wr.println("<h3>Votre url : " + url[0] + " " + url[1] + "</h3>");
        if (methodPresent == null) {
            wr.println("<h3>Aucune methode n'est associee a cette URL</h3>");
            return;
        } else {
            urlMethodMapGET.put(url[1], methodPresent);
            if (!methodPresent.getRequestMethod().equals(method)) {
                wr.println("<h3>Methode appellee :" + methodPresent.getMethod().getName() + "()</h3>");
            } else {

                methodPresent.getMethod().setAccessible(true);
                try {
                    methodPresent.getMethod()
                            .invoke(methodPresent.getMethod().getDeclaringClass().getDeclaredConstructor()
                                    .newInstance());
                    wr.println("<h3>La methode a ete appelee avec succes</h3>");
                } catch (Exception e) {
                    e.printStackTrace(wr);
                }
            }
        }

    }
    
}
