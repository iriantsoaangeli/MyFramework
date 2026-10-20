package angeli.sprint.session;


import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Il donne les instance de Session au classes controller (evite de donner HttpSession)
*/
public class SessionManager {
    HttpServletRequest request;

    public Session getSession(){
        Session session = new Session(this) ;
        HttpSession httpSession = request.getSession();
        Map<String, Object> sessionData = convertToMap(httpSession);
        session.setData(sessionData);
        return session;
    }
    public  void setRequest(HttpServletRequest request){
        this.request = request;
    }

    protected Map<String, Object> convertToMap(HttpSession httpSession){
        Map<String, Object> sessionData = new HashMap<>();
        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = httpSession.getAttribute(attributeName);
            sessionData.put(attributeName, attributeValue);
        }
        return sessionData;
    }
}
