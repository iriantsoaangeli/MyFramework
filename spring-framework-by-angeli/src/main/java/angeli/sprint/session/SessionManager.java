package angeli.sprint.session;


import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


public class SessionManager {
    HttpServletRequest request;
    public SessionManager(HttpServletRequest request) {
        this.request = request;
    }

    public Session getSession(){
        Session session = new Session(this) ;
        session.setData((Map<String, Object>)request.getSession());
        return session;
    }
}
