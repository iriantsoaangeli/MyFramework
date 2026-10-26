package angeli.sprint.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class DataGeter {
    static Map<String,byte[]> getFichiers(HttpServletRequest request) throws IOException, ServletException {
        Part[] parts = request.getParts().toArray(new Part[0]);
        Map<String,byte[]> map = new HashMap<>();
        for (Part part : parts) {
            if (part.getContentType() != null) {
                String name = part.getSubmittedFileName();
                byte[] data = part.getInputStream().readAllBytes();
                map.put(name, data);
            }
        }
        return map;
    }
}
