package angeli.sprint.utils;

import java.io.IOException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class URLParser {

    public static String[] getUrlFromRequest(HttpServletRequest req){
        String url = req.getRequestURL().toString();
        String[] parts = new String[2];
        String serverName = req.getServerName();
        String contextPath = req.getContextPath();
        String scheme = req.getScheme();
        String port = String.valueOf(req.getServerPort());
        String baseURL = scheme + "://" + serverName + ":" + port + contextPath;
        String uri = url.replace(baseURL, "");
        parts[0] = baseURL;
        parts[1] = uri;
        return parts;
    }   

    public static void readUrl(HttpServletRequest req , HttpServletResponse rep) throws IOException,ServletException{
        String method = req.getMethod();
        String url = getUrlFromRequest(req)[1];
        ServletContext  context = req.getServletContext();
        String affix = (String) context.getAttribute("affix");
        String prefix = (String) context.getAttribute("prefix");
        String fullUrl = prefix + url + affix;

        req.getRequestDispatcher(fullUrl).forward(req,rep);;
    }

}
