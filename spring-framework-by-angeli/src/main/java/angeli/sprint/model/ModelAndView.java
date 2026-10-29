package angeli.sprint.model;

import java.util.function.BiConsumer;

/**
 * Retour d'une methode de controller qui affiche une vue.
 * Contient le nom de la vue (sans prefix ni suffix) et permet de passer des
 * donnees a la vue avec {@link #setAttribute(String, Object)}.
 */
public class ModelAndView {
    /** Nom de la vue, sans le dossier ni l'extension. */
    String view;
    /**
     * La methode a appeler pour mettre les attributs dans le context du servlet
     * Comme ca on donne pas le Servlet contexte aux controllers
     */
    static BiConsumer<String, Object> action;

    /**
     * Cree un ModelAndView pour la vue donnee.
     *
     * @param view nom de la vue, exemple : "accueil" pour /WEB-INF/views/accueil.jsp
     */
    public ModelAndView(String view) {
        setView(view);
    }

    /**
     * Set la methode a appeler pour mettre les attributs dans le context du servlet.
     * Appelee une seule fois au demarrage par le ContextListener.
     *
     * @param action fonction (cle, valeur) qui enregistre l'attribut
     */
    public static void setAction(BiConsumer<String, Object> action) {
        ModelAndView.action = action;
    }

    /**
     * Change la vue a afficher.
     *
     * @param view nom de la vue a afficher
     */
    public void setView(String view) {
        this.view = view;
    }

    /**
     * Rend une donnee accessible dans la vue (ex : {@code ${cle}} en JSP).
     * Ne fait rien si aucune action n'a ete definie.
     *
     * @param key   nom de l'attribut
     * @param value valeur de l'attribut
     */
    public void setAttribute(String key, Object value) {
        if (action != null) {
            action.accept(key, value);
        }
    }

    /**
     * Retourne le nom de la vue.
     *
     * @return le nom de la vue
     */
    public String getView() {
        return view;
    }
}
