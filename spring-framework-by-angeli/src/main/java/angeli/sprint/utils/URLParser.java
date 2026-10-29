package angeli.sprint.utils;


import jakarta.servlet.http.HttpServletRequest;

/**
 * Formatteur pour l'url : separe l'url de base de l'application et le chemin demande.
 */
public class URLParser {

    /**
     * Decoupe l'url de la requete.
     * Exemple : http://localhost:8080/test/eleve donne
     * ["http://localhost:8080/test", "/eleve"].
     *
     * @param req la requete http
     * @return un tableau de deux elements, le premier element est l'url de base et le deuxieme element est l'uri
     */
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
