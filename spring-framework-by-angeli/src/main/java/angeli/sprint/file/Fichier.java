package angeli.sprint.file;

/**
 * 
 * Représente un fichier.
 * @param nom nom du fichier
 * @param attribute le nom d'attribut dans la requete http
 * @param data les donnees
 */
public record Fichier(String nom , String attribute ,byte[] data) {}
