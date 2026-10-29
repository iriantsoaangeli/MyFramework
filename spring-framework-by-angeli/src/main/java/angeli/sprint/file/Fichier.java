package angeli.sprint.file;

/**
 * Fichier envoye dans une requete multipart.
 * Peut etre recu tel quel en parametre d'une methode de controller,
 * ou seulement son contenu avec un parametre {@code byte[]}.
 *
 * @param nom       nom du fichier envoye par le client
 * @param attribute nom du champ dans la requete http
 * @param data      contenu du fichier
 */
public record Fichier(String nom , String attribute ,byte[] data) {}
