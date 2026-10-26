package angeli.sprint.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import angeli.sprint.file.Fichier;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class DataGeter {
    static Fichier[] getFichiers(HttpServletRequest request) throws IOException, ServletException {
        Part[] parts = request.getParts().toArray(new Part[0]);
        List<Fichier> fichiers = new ArrayList<>();

        return fichiers.toArray(new Fichier[0]);
    }
}
