package angeli.sprint.url;

import java.lang.reflect.Method;

public class URLMethod {
    Method method;
    String requestMethod;

    public URLMethod(Method method, String requestMethod) {
        this.method = method;
        this.requestMethod = requestMethod;
    }
    @Override
    public String toString(){
        String urlMethod = "Method: " + method.getName() + ", Request Method: " + requestMethod;
        return urlMethod;
    }

    public Method getMethod(){
        return method;
    }
    public String getRequestMethod(){
        return requestMethod;
    }
}
