package angeli.sprint.model;

import java.util.function.BiConsumer;

import javax.swing.Action;

import jakarta.servlet.ServletContext;

/**
 * ModelAndView class
 * L'objet qui prend la vue et les donnees du model pour les passer a la vue
 * 
 * @author Angeli
 */
public class ModelAndView {

    String view;
    static BiConsumer<String, Object> action;

    public ModelAndView(String view, ServletContext context) {
        setView(view);
    }

    public static void setAction(BiConsumer<String, Object> action) {
        ModelAndView.action = action;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setAttribute(String key, Object value) {
        if (action != null) {
            action.accept(key, value);
        }
    }

    public String getView() {
        return view;
    }
}