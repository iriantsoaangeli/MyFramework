package angeli.sprint.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Il donne les instance de Session au classes controller (evite de donner
 * HttpSession)
 */
public class SessionManager {
    HttpServletRequest request;
    Map<String, Session> sessions;

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

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public SessionManager() {
        sessions = new HashMap<>();
    }

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
    public void updateSessionData(Session session) {
        HttpSession httpSession = request.getSession();
        Map<String, Object> sessionData = session.getData();
        for (Map.Entry<String, Object> entry : sessionData.entrySet()) {
            httpSession.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
