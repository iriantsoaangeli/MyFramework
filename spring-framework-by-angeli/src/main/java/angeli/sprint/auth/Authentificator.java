package angeli.sprint.auth;

import angeli.sprint.exception.AuthException;
import jakarta.servlet.http.HttpSession;

/**
 * Classe qui gère l'authentification des utilisateurs.
 * @see angeli.sprint.annotation.Auth
 */
public class Authentificator {
   public static void checkAuth(String authKey , HttpSession session) throws AuthException {
    System.out.println("Vérification de l'authentification avec la clé : " + authKey);
        if (session.getAttribute(authKey) == null || session.getAttribute(authKey).toString().isEmpty()) {
            throw new AuthException("Vous n'avez pas l'autorisation d'accéder à cette ressource");
        }
    }
}
