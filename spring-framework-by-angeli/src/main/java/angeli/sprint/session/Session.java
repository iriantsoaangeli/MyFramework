package angeli.sprint.session;

import java.util.Map;

/**
 * Classe qui encapsule les donnees de session pour les controller.
 * A recevoir en parametre d'une methode de controller ; les modifications
 * sont recopiees dans la HttpSession apres l'appel de la methode.
 * Plusieurs methodes sont des alias (set/put/setAttribute, remove/removeAttribute...).
 */
public class Session {
    /** Les attributs de la session. */
    Map<String, Object> data;
    /** Le manager qui a cree cette session. */
    SessionManager sessionManager;

    /**
     * Cree une session vide, les donnees sont donnees ensuite par le manager.
     *
     * @param sessionManager le manager qui cree la session
     */
    public Session(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Ajoute ou remplace un attribut.
     *
     * @param key   nom de l'attribut
     * @param value valeur de l'attribut
     */
    public void set(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Alias de {@link #set(String, Object)}.
     *
     * @param key   nom de l'attribut
     * @param value valeur de l'attribut
     */
    public void setAttribute(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Supprime un attribut.
     *
     * @param key nom de l'attribut
     */
    public void remove(String key) {
        data.remove(key);
    }

    /**
     * Alias de {@link #remove(String)}.
     *
     * @param key nom de l'attribut
     */
    public void removeAttribute(String key) {
        data.remove(key);
    }

    /**
     * Supprime tous les attributs.
     */
    public void clear() {
        data.clear();
    }

    /**
     * Alias de {@link #clear()}.
     */
    public void clearAttributes() {
        data.clear();
    }

    /**
     * Alias de {@link #set(String, Object)}.
     *
     * @param key   nom de l'attribut
     * @param value valeur de l'attribut
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Retourne la valeur de l'attribut, null si absent.
     *
     * @param key nom de l'attribut
     * @return la valeur de l'attribut, null si absent
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Retourne tous les attributs de la session.
     *
     * @return tous les attributs de la session
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Remplace tous les attributs de la session.
     *
     * @param data les attributs de la session
     */
    void setData(Map<String, Object> data) {
        this.data = data;
    }

}
