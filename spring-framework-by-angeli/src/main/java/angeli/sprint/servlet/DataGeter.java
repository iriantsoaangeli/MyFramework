package angeli.sprint.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import angeli.sprint.file.Fichier;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class DataGeter {
    static Map<String,Fichier> getFichiers(HttpServletRequest request) throws IOException, ServletException {
        Part[] parts = request.getParts().toArray(new Part[0]);
        Map<String,Fichier> map = new HashMap<>();
        for (Part part : parts) {
            if (part.getContentType() != null) {
                String name = part.getSubmittedFileName();
                byte[] data = part.getInputStream().readAllBytes();
                map.put(name, new Fichier(name, part.getName(), data));
            }
        }
        return map;
    }
    static Map<String,String[]> addFileNamesToMap(Map<String,Fichier> fichiers , Map<String,String[]> args){
        if (fichiers == null) {
            return args;
        }
        for (String filename : fichiers.keySet()) {
            args = new HashMap<>(args);
            args.put(fichiers.get(filename).attribute(), new String[]{filename});
        }
        return args;
    }
}
