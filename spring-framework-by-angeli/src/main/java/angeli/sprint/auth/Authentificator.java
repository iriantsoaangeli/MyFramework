package angeli.sprint.auth;

import java.lang.reflect.Method;

import angeli.sprint.exception.AuthException;
import angeli.sprint.exception.RoleException;
import jakarta.servlet.http.HttpSession;

/**
 * Classe qui gère l'authentification des utilisateurs.
 * 
 * @see angeli.sprint.annotation.Auth
 */
public class Authentificator {
    public static void checkAuth(String authKey, HttpSession session, Method calledMethod, String roleKey)
            throws AuthException {
        System.out.println("Vérification de l'authentification avec la clé : " + authKey);
        if (session.getAttribute(authKey) == null || session.getAttribute(authKey).toString().isEmpty()) {
            throw new AuthException("Vous n'avez pas l'autorisation d'accéder à cette ressource");
        }
        if (calledMethod.getAnnotation(angeli.sprint.annotation.Auth.class).roles().length == 0) {
            System.out.println("Pas de role necessaire");
            return;
        }
        else if (roleKey != null && session.getAttribute(roleKey) != null
                && !session.getAttribute(roleKey).toString().isEmpty()) {
                    System.out.println("Il faut verifier le role");
            for (String role : calledMethod.getAnnotation(angeli.sprint.annotation.Auth.class).roles()) {
                if (role.equals(session.getAttribute(roleKey)))
                    return;
            }
        }
        throw new RoleException("Vous n'avez pas le role necessaire pour acceder a cette resource");
    }
}
