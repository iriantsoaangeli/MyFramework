package angeli.sprint.model;

import jakarta.servlet.ServletContext;

/**
 * ModelAndView class
 * L'objet qui prend la vue et les donnees du model pour les passer a la vue
 * 
 * @author Angeli
 */
public class ModelAndView {

    String view;

    public ModelAndView(String view, ServletContext context) {
        setView(view);
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setAttribute(String key, Object value, ServletContext context) {
        context.setAttribute(key, value);
    }

    public String getView() {
        return view;
    }
}