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

 
}
