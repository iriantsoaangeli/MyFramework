package angeli.sprint.url;

import java.lang.reflect.Method;

public class URLMethod {
    Method method;
    String requestMethod;

    public URLMethod(Method method, String requestMethod) {
        this.method = method;
        this.requestMethod = requestMethod;
    }

}
