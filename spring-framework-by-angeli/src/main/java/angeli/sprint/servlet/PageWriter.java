package angeli.sprint.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Ecrit le resultat d'une methode de controller dans la reponse http
 * (JSON, HTML ou page 404).
 */
public class PageWriter {

    /**
     * Ecrit une page d'erreur 404 simple.
     *
     * @param rep la reponse http
     * @throws IOException si la reponse ne peut pas etre ecrite
     */
    public static void urlNotFound(HttpServletResponse rep) throws IOException {
        PrintWriter wr = rep.getWriter();
        wr.println("<h1>error 404</h1>");
    }

    /**
     * Serialise l'objet en JSON avec Jackson et l'ecrit.
     *
     * @param wr le writer de la reponse
     * @param o  l'objet a serialiser
     * @throws IOException si la serialisation ou l'ecriture echoue
     */
    public static void printJson(PrintWriter wr, Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(o);
        wr.print(json);
    }

    /**
     * Ecrit l'objet tel quel (via toString()).
     *
     * @param wr le writer de la reponse
     * @param o  l'objet a ecrire
     */
    public static void printHtml(PrintWriter wr, Object o) {
        wr.print(o);
    }

    /**
     * Print manuel dans la reponse , par defaut json.
     * Seuls "application/json" et "text/html" sont geres, tout autre
     * Content-Type (ou null) donne du JSON.
     *
     * @param req         la requete http
     * @param rep         la reponse http
     * @param message     l'objet a ecrire
     * @param contentType le Content-Type voulu, peut etre null
     * @throws IOException si la reponse ne peut pas etre ecrite
     */
    public static void print(HttpServletRequest req, HttpServletResponse rep, Object message, String contentType)
            throws IOException {
        PrintWriter wr = rep.getWriter();

        if (contentType != null) {
            rep.setContentType(contentType);
            switch (contentType) {
                case "application/json":
                    printJson(wr, message);
                    break;

                case "text/html":
                    printHtml(wr, message);
                    break;
                default:
                    printJson(wr, message);
                    rep.setContentType("application/json");
                    break;
            }
        } else {
            printJson(wr, message);
            rep.setContentType("application/json");
        }
    }
}
