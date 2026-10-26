package angeli.sprint.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Il donne les instance de Session au classes controller (evite de donner
 * HttpSession).
 * Une seule instance pour l'application, stockee dans le context du servlet ;
 * la requete courante est mise a jour a chaque appel du servlet.
 */
public class SessionManager {
    /** La requete en cours de traitement. */
    HttpServletRequest request;
    /** Les sessions deja creees, par id de HttpSession. */
    Map<String, Session> sessions;

    /**
     * Retourne la Session de la requete courante, la cree (avec les attributs
     * de la HttpSession) si elle n'existe pas encore.
     *
     * @return la session du client
     */
    public Session getSession() {
        HttpSession httpSession = request.getSession();
        if (httpSession.isNew() || sessions.get(httpSession.getId()) == null) {
            Session session = new Session(this);
            Map<String, Object> sessionData = convertToMap(httpSession);
            sessions.put(httpSession.getId(), session);
            session.setData(sessionData);
            return session;
        } else {
            return sessions.get(httpSession.getId());
        }
    }

    /**
     * Definit la requete courante, appele a chaque requete par le servlet.
     *
     * @param request la requete en cours de traitement
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Cree un manager sans session.
     */
    public SessionManager() {
        sessions = new HashMap<>();
    }

    /**
     * Copie les attributs d'une HttpSession dans une Map.
     *
     * @param httpSession la session http
     * @return les attributs par nom
     */
    protected Map<String, Object> convertToMap(HttpSession httpSession) {
        Map<String, Object> sessionData = new HashMap<>();
        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = httpSession.getAttribute(attributeName);
            sessionData.put(attributeName, attributeValue);
        }
        return sessionData;
    }
    /**
     * Recopie les attributs de la Session dans la HttpSession de la requete
     * courante. Les attributs supprimes ne sont pas retires de la HttpSession.
     *
     * @param session la session modifiee par le controller
     */
    public void updateSessionData(Session session) {
        HttpSession httpSession = request.getSession();
        Map<String, Object> sessionData = session.getData();
        for (Map.Entry<String, Object> entry : sessionData.entrySet()) {
            httpSession.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
