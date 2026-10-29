package angeli.sprint.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import angeli.sprint.file.Fichier;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

/**
 * Recupere les fichiers d'une requete multipart et les rend accessibles
 * comme des parametres normaux.
 */
public class DataGeter {
    /**
     * Lit toutes les parts qui ont un Content-Type (donc les fichiers).
     *
     * @param request la requete multipart
     * @return les fichiers indexes par leur nom de fichier
     * @throws IOException      si un fichier ne peut pas etre lu
     * @throws ServletException si la requete n'est pas multipart
     */
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

    /**
     * Ajoute aux parametres l'entree "nom du champ = nom du fichier" pour
     * chaque fichier, afin de retrouver le fichier a partir du nom du parametre.
     *
     * @param fichiers les fichiers de la requete, peut etre null
     * @param args     les parametres de la requete
     * @return une copie des parametres avec les fichiers, ou args si pas de fichiers
     */
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
