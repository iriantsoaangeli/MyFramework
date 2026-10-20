package angeli.sprint.session;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Classe qui encapsule les donnees de session pour les controller
*/
public class Session {
    Map<String, Object> data;
    SessionManager sessionManager;

    public Session(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    Map<String, Object> getData() {
        return data;
    }

    void setData(Map<String, Object> data) {
        this.data = data;
    }

}
