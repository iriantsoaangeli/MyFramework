package angeli.sprint.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    Map<String, Object> data;

    public Session() {
        data = new HashMap<String, Object>();
    }

    public Session(Map<String, Object> data) {
        this.data = data;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }
    public Object get(String key){
        return data.get(key);
    }
    public Map<String, Object> getData(){
        return data;
    }
}
