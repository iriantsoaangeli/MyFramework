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

    public void setAttribute(String key, Object value) {
        data.put(key, value);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public void removeAttribute(String key) {
        data.remove(key);
    }

    public void clear() {
        data.clear();
    }

    public void clearAttributes() {
        data.clear();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Map<String, Object> getData() {
        return data;
    }

    void setData(Map<String, Object> data) {
        this.data = data;
    }

}
